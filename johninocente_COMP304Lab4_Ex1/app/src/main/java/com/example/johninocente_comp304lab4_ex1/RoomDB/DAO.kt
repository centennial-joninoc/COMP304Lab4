package com.example.johninocente_comp304lab4_ex1.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.johninocente_comp304lab4_ex1.Model.SavePlaceData

@Dao
interface SavedPlacesDAO {
    @Query("SELECT * FROM SavedPlaces")
    suspend fun getAllSavedPlaces(): List<SavePlaceData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData( data: SavePlaceData)

}