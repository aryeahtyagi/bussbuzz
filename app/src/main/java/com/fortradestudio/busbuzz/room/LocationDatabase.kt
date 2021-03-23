package com.fortradestudio.busbuzz.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Locations::class),version = 2,exportSchema = true)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDao():LocationDao

    companion object{

        @Volatile
        private var instance:LocationDatabase? = null

        fun getLocationDatabase(context: Context):LocationDatabase {

            var temp = instance

            return if (temp != null) {
                temp;
            } else {
                // here instance is not null
                instance = Room.databaseBuilder(
                    context, LocationDatabase::class.java, "location_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                temp = instance
                temp!!

            }
        }

    }

}