package com.example.johninocente_comp304lab4_ex1.Model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng


@Entity(tableName = "SavedPlaces")
data class SavePlaceData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val displayName: String,
    @DrawableRes val placeImg: Int,
    val rating: Float,
    val imgURL: String = "",
) {
    constructor(
        id: Int,
        latLng: LatLng,
        displayName: String,
        @DrawableRes placeImg: Int,
        rating: Float, imgURL: String = ""
    ) :
        this(id, latLng.latitude, latLng.longitude, displayName, placeImg, rating, imgURL)
}