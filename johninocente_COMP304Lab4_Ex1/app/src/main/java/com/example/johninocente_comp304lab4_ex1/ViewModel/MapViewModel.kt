package com.example.johninocente_comp304lab4_ex1.ViewModel


import androidx.lifecycle.ViewModel
import com.example.johninocente_comp304lab4_ex1.Model.SavePlaceData
import com.example.johninocente_comp304lab4_ex1.datasamples.SavedPlacesDataSample

class MapViewModel: ViewModel() {
    private val repository = SavedPlacesDataSample()

    fun getSavedPlaces() : List<SavePlaceData> { return repository.getSavedPlaces() }
}