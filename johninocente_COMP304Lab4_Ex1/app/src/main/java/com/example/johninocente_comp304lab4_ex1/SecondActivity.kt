package com.example.johninocente_comp304lab4_ex1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil3.compose.AsyncImage
import com.example.johninocente_comp304lab4_ex1.LogDataWorker.DataLogger
import com.example.johninocente_comp304lab4_ex1.Model.SavePlaceData
import com.example.johninocente_comp304lab4_ex1.RoomDB.AppDatabase
import com.example.johninocente_comp304lab4_ex1.View.theme.Johninocente_COMP304Lab4_Ex1Theme
import com.example.johninocente_comp304lab4_ex1.ViewModel.MapViewModel

class SecondActivity : ComponentActivity() {
    //WorkManager
    private lateinit var workManager : WorkManager
    var idsForLog = emptyList<Int>()
    var latitudesForLog = emptyList<Double>()
    var longitudesForLog = emptyList<Double>()
    var displayNamesForLog = emptyList<String>()
    var placeImgsForLog = emptyList<Int>()
    var ratingsForLog = emptyList<Float>()
    var imgURLsForLog = emptyList<String>()

    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        workManager = WorkManager.getInstance(applicationContext)

        setContent {
            Johninocente_COMP304Lab4_Ex1Theme {
                Scaffold (
                    topBar = { TopAppBarUI() }
                ) { innerPadding ->
                    ContentUI(innerPadding = innerPadding)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()

        Log.d("Lab4app","Work manager start" )
        var request = OneTimeWorkRequestBuilder<DataLogger>().setInputData(
            workDataOf(
                "ids" to idsForLog.toTypedArray(),
                "latitudes" to latitudesForLog.toTypedArray(),
                "longitudes" to longitudesForLog.toTypedArray(),
                "displayNames" to displayNamesForLog.toTypedArray() ,
                "placeImgs" to placeImgsForLog.toTypedArray(),
                "ratings" to ratingsForLog.toTypedArray(),
                "imgURLs" to imgURLsForLog.toTypedArray()
            )).setConstraints(
            Constraints(
                // Using RoomDB so this will not be used
                // requiredNetworkType = NetworkType.CONNECTED
            )
        ).build()

        workManager.enqueue(request)

        Log.d("Lab4app","Work manager finished" )
        idsForLog = emptyList()
        latitudesForLog = emptyList()
        longitudesForLog = emptyList()
        displayNamesForLog = emptyList()
        placeImgsForLog = emptyList()
        ratingsForLog = emptyList()
        imgURLsForLog = emptyList()
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun TopAppBarUI() {
        TopAppBar(
            title = { Text(text = "Pick your place 🪧") },
            colors = TopAppBarDefaults.topAppBarColors(
                titleContentColor = MaterialTheme.colorScheme.primary,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            )
        )
    }

    @Composable
    private fun ContentUI(innerPadding: PaddingValues) {

        val placesList = mapViewModel.getSavedPlaces()

        LazyColumn (
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(10.dp),
            userScrollEnabled = true,
        ) {
            items(placesList) { place ->
                CardUI(place)

                // For workmanager, add data to the arrays
                idsForLog += place.id
                latitudesForLog += place.latitude
                longitudesForLog += place.longitude
                displayNamesForLog += place.displayName
                placeImgsForLog += place.placeImg
                ratingsForLog += place.rating
                imgURLsForLog += place.imgURL

            }
        }
    }

    @Composable
    private fun CardUI(placeData: SavePlaceData) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .fillMaxSize(),
            contentAlignment = Alignment.TopStart
        )
        {
            AsyncImage(
                model = placeData.imgURL,
                contentDescription = "Image About",
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { gotoMapActivity(this@SecondActivity, placeData) }
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Row (
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 20.dp)
                    .background(color = Color.Black.copy(alpha = 0.5f))
                    .align(Alignment.BottomEnd)
            ) {
                // Title Text
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .padding(start = 10.dp)
                        .fillMaxWidth(.7f),
                    text = placeData.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,

                    )

                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .padding(end = 10.dp)
                        .fillMaxWidth(),
                    text = "⭐ ${placeData.rating}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }

    // Go to Main activity after selecting a Place
    private fun gotoMapActivity(context: Context, placeData: SavePlaceData) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("Place_Location_Lattitude", placeData.latitude)
        intent.putExtra("Place_Location_Longitude", placeData.longitude)
        intent.putExtra("Place_Name", placeData.displayName)
        context.startActivity(intent)
    }
}