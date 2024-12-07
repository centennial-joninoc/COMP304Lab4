package com.example.johninocente_comp304lab4_ex1.View

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.johninocente_comp304lab4_ex1.GeofencingLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapView(modifier: Modifier,
            userLocation: LatLng?,
            displayName: String,
            selectedPlacePos: LatLng?,
            cameraPositionState: CameraPositionState,
            uiSettings: MapUiSettings,
            properties: MapProperties,
            onMapClick : (LatLng) -> Unit) {

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = uiSettings,
        properties = properties,
        onMapClick = { latLng ->
            onMapClick(latLng)
        }
    ) {

        if (selectedPlacePos != LatLng(0.0,0.0))
        {
            Log.d("Lab4App","selected: 0 0" )
            // make marker for selected place
            selectedPlacePos?.let {
                Marker(
                    state = MarkerState(position = selectedPlacePos),
                    title = displayName,
                    onClick = {
                        true
                    }
                )

                cameraPositionState.move(
                    CameraUpdateFactory.newLatLng(selectedPlacePos)
                )

                // wanted to use a line like a directions towards the destination
                // but it has another api called directions and its not free.
                // so I am drawing the line to know the distance for the geofence toast to pop up.
                if (userLocation != null)
                {
                    Polyline(
                        points = listOf(userLocation, selectedPlacePos),
                        color = Color.Red,
                        width = 10f,
                        geodesic = true
                    )
                }

                Log.d("Lab4App","Adding Geofence from Selected Place" )
                GeofencingLocation(selectedPlacePos)
            }
        }

        userLocation?.let{
            cameraPositionState.move(
                CameraUpdateFactory.newLatLng(userLocation)
            )
        }
    }
}