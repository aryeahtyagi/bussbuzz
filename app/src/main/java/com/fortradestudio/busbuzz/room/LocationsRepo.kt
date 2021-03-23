package com.fortradestudio.busbuzz.room

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationsRepo(private val dao: LocationDao) {

    fun insertToDb(locations: Locations) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insertToDb(locations)
        }
    }

    fun deleteFromDb(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteLocation(id)
        }
    }

    fun getLocations() = dao.getLocations()


}