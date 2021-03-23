package com.fortradestudio.busbuzz.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlin.math.sin

private const val TAG = "LocationRepositoryImpl"

class LocationRepositoryImpl : LocationRepository {


    @SuppressLint("MissingPermission")
    override fun getUserLocationCoordinates(context: Context, target: Location?,requestInterval:Long,fastestInterval:Long, onLocationFetched:(LatLng)->Unit, onDistanceFetched: (Double) -> Unit) {

        val mLocationRequest: LocationRequest = LocationRequest.create()
        mLocationRequest.interval = requestInterval
        mLocationRequest.fastestInterval = fastestInterval
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.getLocations()) {
                    if (location != null) {
                        onLocationFetched(LatLng(location.latitude,location.longitude))

                        if(target != null) {
                            val distance = checkIfUserWithinTargetLocation(
                                Location(
                                    location.latitude,
                                    location.longitude
                                ), target
                            )
                            onDistanceFetched(distance)
                        }
                    }
                }
            }
        }

        if (context != null) {
            LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(mLocationRequest,mLocationCallback,null)
        }

    }

    override fun getUserLocationStringFromApi(location: Location) {
        TODO("Not yet implemented")
    }

    override fun checkIfUserWithinTargetLocation(location: Location, target: Location): Double {
        return calculateLinearDistance(location,target)
    }

    fun calculateLinearDistance(location: Location, target: Location): Double {
        val R = 6378.137; // Radius of earth in KM
        val dLat = target.latitude * Math.PI / 180 - location.latitude * Math.PI / 180;
        val dLon = target.longitude * Math.PI / 180 - location.longitude * Math.PI / 180;
        val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(location.latitude * Math.PI / 180) * Math.cos(target.latitude * Math.PI / 180) *
                Math.sin(dLon/2) * sin(dLon/2);
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        val d = R * c;

        return d * 1000
    }

}