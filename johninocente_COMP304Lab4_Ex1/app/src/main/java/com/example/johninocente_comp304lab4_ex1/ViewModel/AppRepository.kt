package com.example.johninocente_comp304lab4_ex1.ViewModel

import com.example.johninocente_comp304lab4_ex1.Model.SavePlaceData
import com.example.johninocente_comp304lab4_ex1.RoomDB.SavedPlacesDAO

class AppRepository(private val dao: SavedPlacesDAO)
{
    suspend fun GetAllLogs() : List<SavePlaceData>
    {
        return dao.getAllSavedPlaces()
    }

    suspend fun InsertData(data : SavePlaceData)
    {
        dao.insertData(data)
    }
}