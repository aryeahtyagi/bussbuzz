package com.fortradestudio.busbuzz.location

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng

interface LocationRepository {

    fun getUserLocationCoordinates(context: Context, target: Location?=null,requestInterval:Long=10_000,fastestInterval:Long=5000, onLocationFetched:(LatLng)->Unit, onDistanceFetched:(Double)->Unit)

    @Deprecated(message = "there is no need for this function i guess")
    fun getUserLocationStringFromApi(location: Location)

    fun checkIfUserWithinTargetLocation(location: Location,target:Location):Double

}