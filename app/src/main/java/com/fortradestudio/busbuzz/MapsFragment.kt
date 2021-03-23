package com.fortradestudio.busbuzz

import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class MapsFragment : Fragment() {

    lateinit var mapsFragment: SupportMapFragment

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(20.5937,78.9629)
        googleMap.addMarker(MarkerOptions().position(sydney).title("INDIA"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapsFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapsFragment.getMapAsync(callback)


    }

    public fun updateLocation(yourLocation:LatLng,searchLocation:LatLng?=null,onLocationSelected:(LatLng)->Unit){

        mapsFragment.getMapAsync {

            it.clear()

            it.addMarker(MarkerOptions()
                .position(yourLocation)
                .title("You")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

            it.moveCamera(CameraUpdateFactory.newLatLngZoom(yourLocation,18f))

            if(searchLocation!=null){
                it.addMarker(MarkerOptions()
                    .position(searchLocation)
                    .title("Target")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

                it.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLocation,11f))
            }


            it.setOnMapClickListener { latLng->

                it.clear()

                onLocationSelected(latLng)


                it.addMarker(MarkerOptions().position(yourLocation)
                    .title("You")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))

                it.addMarker(MarkerOptions()
                    .position(latLng)
                    .title("Target")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))

            }

        }
    }

}