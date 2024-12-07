package com.example.johninocente_comp304lab4_ex1.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.johninocente_comp304lab4_ex1.Model.SavePlaceData

@Database(entities = [SavePlaceData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val savePlacesDao: SavedPlacesDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            // ensuring that only one thread can execute the block
            // of code inside the synchronized block at any given time
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    // Creating the Database Object
                    instance = Room.databaseBuilder(
                        context = context,
                        AppDatabase::class.java,
                        "User_SavedPlacesDB"
                    ).build()
                }
                INSTANCE = instance
                return instance
            }
        }
    }
}