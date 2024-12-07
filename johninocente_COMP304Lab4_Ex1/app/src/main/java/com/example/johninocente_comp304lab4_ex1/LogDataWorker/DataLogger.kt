package com.example.johninocente_comp304lab4_ex1.LogDataWorker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.johninocente_comp304lab4_ex1.Model.SavePlaceData
import com.example.johninocente_comp304lab4_ex1.RoomDB.AppDatabase
import com.google.android.gms.maps.model.LatLng

class DataLogger(private val appContext: Context,
                     private val param: WorkerParameters) : CoroutineWorker(appContext, param) {

    override suspend fun doWork(): Result {

        var database = AppDatabase.getInstance(applicationContext)
        var done = 0;
        var ids = param.inputData.getIntArray("ids");
        var latitudes = param.inputData.getDoubleArray("latitudes");
        var longitudes = param.inputData.getDoubleArray("longitudes");
        var displayNames = param.inputData.getStringArray("displayNames");
        var placeImgs = param.inputData.getIntArray("placeImgs");
        var ratings = param.inputData.getFloatArray("ratings");
        var imgURLs = param.inputData.getStringArray("imgURLs");

        var counter = 0
        if (ids != null) {
            while (counter < ids.size){
                val locationLog = SavePlaceData(
                    id = ids[counter],
                    latitude = latitudes?.get(counter) ?: 0.0,
                    longitude = longitudes?.get(counter) ?: 0.0,
                    displayName = displayNames?.get(counter) ?: "",
                    placeImg = placeImgs?.get(counter) ?: 0,
                    rating = ratings?.get(counter) ?: 0.0f,
                    imgURL = imgURLs?.get(counter) ?: ""
                )

                database.savePlacesDao.insertData(locationLog)
                done = 1;
                counter++
            }
        }
        if (done == 1) {
            return Result.success()
        }else
            return  Result.failure()
    }
}