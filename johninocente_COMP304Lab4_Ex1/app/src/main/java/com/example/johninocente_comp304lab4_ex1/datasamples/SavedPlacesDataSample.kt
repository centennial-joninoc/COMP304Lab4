package com.example.johninocente_comp304lab4_ex1.datasamples

import com.example.johninocente_comp304lab4_ex1.R
import com.example.johninocente_comp304lab4_ex1.Model.SavePlaceData
import com.google.android.gms.maps.model.LatLng

class SavedPlacesDataSample {

    private val savedPlacesSamples = mutableListOf<SavePlaceData>(
        SavePlaceData(1, LatLng(43.784966465512746, -79.23028276052024), "The Local Cafe and Restaurant", R.drawable.restaurant, 4.5f, "https://images.squarespace-cdn.com/content/v1/567d5140841abaa3c9136278/1501264407970-ITCQAZT37RQ8YP3ES1FT/image-asset.jpeg"),
        SavePlaceData(2, LatLng(43.77595537594554, -79.23890360054902), "Gwalia Sweets and Restaurant - Scarborough", R.drawable.restaurant, 4.6f, "https://i.ytimg.com/vi/_IuYTw-jv4U/maxresdefault.jpg"),
        SavePlaceData(6, LatLng(43.78482718707051, -79.22730627463008), "Tim Hortons", R.drawable.local_cafe, 3.2f, "https://brandslogos.com/wp-content/uploads/images/large/tim-hortons-logo.png"),
        SavePlaceData(9, LatLng(43.783364702650985, -79.23566518788873), "Holiday Inn Express Toronto East - Scarborough, an IHG Hotel", R.drawable.hotel, 3.8f, "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1c/8e/f8/ac/hotel-exterior.jpg?w=900&h=-1&s=1"),
        SavePlaceData(10, LatLng(43.671997472639475, -79.39022774886321), "Four Seasons Hotel Toronto", R.drawable.hotel, 4.7f, "https://www.oyster.com/wp-content/uploads/sites/35/2019/05/exterior-v17919128-1440-1024x683.jpg"),
        SavePlaceData(13, LatLng(43.79308915525651, -79.24027464982998), "Food Basics", R.drawable.local_grocery_store, 4.0f, "https://cdn.canada247.info/assets/uploads/b8253e1cab973b3c3705b1f09a9215b5_-ontario-hamilton-division-hamilton-berrisfield-food-basicshtml.jpg"),
        SavePlaceData(14, LatLng(43.77779493688872, -79.25880895630233), "Walmart Supercentre", R.drawable.local_grocery_store, 3.7f, "https://imageio.forbes.com/specials-images/imageserve/63e19d0c2b2fa6989a4c9761/0x0.jpg?format=jpg&crop=3175,1447,x0,y21,safe&height=900&width=1600&fit=bounds"),
        SavePlaceData(14, LatLng(43.7776739556713, -79.231464556116), "Shell", R.drawable.local_gas_station, 3.4f, "https://northalsted.com/wp-content/uploads/2023/04/Circle-K-Business-Photo.jpg"),
        SavePlaceData(16, LatLng(43.772406076311405, -79.2518922601135), "Esso", R.drawable.local_gas_station, 4.0f, "https://static.bangkokpost.com/media/content/dcx/2023/09/20/4897427.jpg"),
        SavePlaceData(17, LatLng(43.78563462626393, -79.19518049182292), "Petro-Canada & Car Wash", R.drawable.local_gas_station, 3.7f, "https://www.petro-canada.ca/-/media/project/petrocanada/shared/navigation-cards/our-story.jpg?la=en&mw=1144&modified=20181116231339&hash=2C74F5C910D2D08EECB9E7AF39A68E72BE779107"),
        )

    fun getSavedPlaces() : List<SavePlaceData> { return savedPlacesSamples }
}