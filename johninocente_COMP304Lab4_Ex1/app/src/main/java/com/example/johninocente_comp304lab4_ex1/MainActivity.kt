package com.example.johninocente_comp304lab4_ex1

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.WorkManager
import com.example.johninocente_comp304lab4_ex1.GeoFence.addGeofence
import com.example.johninocente_comp304lab4_ex1.GeoFence.createGeofence
import com.example.johninocente_comp304lab4_ex1.View.MapView
import com.example.johninocente_comp304lab4_ex1.View.theme.Johninocente_COMP304Lab4_Ex1Theme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.PointOfInterest
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    //Google Maps
    private var selectedPlacePos: LatLng = LatLng(0.0,0.0)

    //Default Selected Location: Centennial College Progress Ave.
    private var myPos: LatLng = LatLng(43.7852, -79.2282)
    private var displayName: String = ""

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_api_key))
        }

        enableEdgeToEdge()
        setContent {
            Johninocente_COMP304Lab4_Ex1Theme {
                AskForLocationPermission()

                Surface(modifier = Modifier.fillMaxSize())
                {
                    extractDataFromIntent()

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "Map View",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        color = Color.Black)
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                    containerColor = MaterialTheme.colorScheme.primaryContainer)
                            )
                        },
                        bottomBar = { BottomAppBarUI() }
                    ) {
                        MapUI()
                    }
                }
            }
        }
    }

    @Composable
    private fun AskForLocationPermission() {
        // Handle permission requests for accessing fine location
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {

            } else {

            }
        }

        LaunchedEffect(Unit) {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    // We have the location permission
                }
                else -> {
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    @Preview
    @Composable
    private fun MapUI() {
        ContentUI(PaddingValues(0.dp))
    }

    @Composable
    private fun ContentUI(innerPadding: PaddingValues) {

        val isMyLocationAvailable = hasLocationPermission()
        val coroutineForCallbacks = rememberCoroutineScope()
        val cameraPosState = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(selectedPlacePos, 15f) }
        val properties = remember { MapProperties(isBuildingEnabled = true, isMyLocationEnabled = isMyLocationAvailable) }
        val uiSettings = remember { MapUiSettings(mapToolbarEnabled = false, zoomControlsEnabled = false, compassEnabled = false, myLocationButtonEnabled = false) }

        val context = LocalContext.current
        var userLocation by remember { mutableStateOf<LatLng?>(null) }
        val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
        val locationRequest = remember {
            LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 10000 // Update every 10 seconds
                fastestInterval = 5000
            }
        }
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
        }
        // Request location updates
        LaunchedEffect(Unit) {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                            location ->
                        val newLocation = LatLng(location.latitude, location.longitude)
                        userLocation = newLocation
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLng(newLocation)
                        )
                    }
                }
            }

            if (userLocation == null) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                }
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    context.mainLooper
                )
            }
        }

        MapView(
            modifier = Modifier.fillMaxSize(),
            userLocation,
            displayName,
            selectedPlacePos,
            cameraPosState,
            uiSettings,
            properties,
            onMapClick = { latLng ->
                cameraPosState.move(
                    CameraUpdateFactory.newLatLng(latLng)
                )
            })

        MapInteractableButtons(isMyLocationAvailable = isMyLocationAvailable) {
            coroutineForCallbacks.launch {
                cameraPosState.animate(CameraUpdateFactory.newLatLng(it), 2000)
            }
        }
    }

    @Composable
    private fun MapInteractableButtons(isMyLocationAvailable: Boolean, onFabClicked: (LatLng) -> Unit) {
        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd,
        ) {
            if (isMyLocationAvailable) {
                SmallFloatingActionButton(
                    onClick = { onFabClicked(myPos) },
                    modifier = Modifier.padding(top = 50.dp, end = 30.dp, bottom = 200.dp),
                    shape = CircleShape,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.my_location),
                        contentDescription = "Re-Center"
                    )
                }
            }
            SmallFloatingActionButton(
                onClick = { onFabClicked(selectedPlacePos) },
                modifier = Modifier.padding(top = 100.dp, end = 30.dp, bottom = 130.dp),
                shape = CircleShape,
            ) {
                Icon(imageVector = Icons.Rounded.LocationOn, contentDescription = "Re-Center")
            }
        }
    }

    private fun Context.hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @Composable
    private fun BottomAppBarUI() {
        BottomAppBar(
            actions = {
                Row (
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(onClick = { gotoListActivity(this@MainActivity) }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "Saved Places")
                    }
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun extractDataFromIntent() {

        var lat = intent.getDoubleExtra("Place_Location_Lattitude", myPos.latitude)
        var lng = intent.getDoubleExtra("Place_Location_Longitude", myPos.longitude)

        if (lat != myPos.latitude && lng != myPos.longitude)
        {
            Log.d("Lab4App","lat: $lat, long: $lng" )
            selectedPlacePos = LatLng(lat, lng)
            displayName = intent.getStringExtra("Place_Name").toString()
        }
    }

}

private fun gotoListActivity(context: Context) {
    val intent = Intent(context, SecondActivity::class.java)
    context.startActivity(intent)
}

@Composable
fun GeofencingLocation(gfLocation: LatLng) {
    val context = LocalContext.current
    val geofenceId = "ExampleGeofence"
    val geofenceRadius = 200f // 200 meters

    LaunchedEffect(Unit) {
        val geofence = createGeofence(geofenceId, gfLocation, geofenceRadius)
        addGeofence(context, geofence)
    }
}


