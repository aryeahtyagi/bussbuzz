package com.fortradestudio.busbuzz.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert
    fun insertToDb(locations: Locations)

    @Query("Select * from locations")
    fun getLocations():LiveData<List<Locations>>

    @Query("Delete from locations where id=:id")
    fun deleteLocation(id:Int)

}