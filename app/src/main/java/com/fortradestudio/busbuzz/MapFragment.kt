package com.fortradestudio.busbuzz

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.fortradestudio.busbuzz.fileUtils.FileRepositoryImpl
import com.fortradestudio.busbuzz.location.LocationRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapFragment : Fragment() {


    private var finalLocation : String = "default"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.maps) as MapsFragment
        val searchAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.searchLocation)
        val locateButton = view.findViewById<AppCompatButton>(R.id.locateButton)
        val reminder = view.findViewById<AppCompatButton>(R.id.reminderBtn)

        val fileRepo = FileRepositoryImpl()

        

        reminder.setOnClickListener {
            if(finalLocation == "default"){
                Toast.makeText(requireContext(),"Please Select target destination",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val bundle = Bundle().apply {
                putString("location",finalLocation)
            }
            findNavController().navigate(R.id.action_mapFragment_to_reminderFragment,bundle)
        }


        fileRepo.getCitiesFromFile(requireContext()) {
            CoroutineScope(Dispatchers.Main).launch {
                val arrayAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_dropdown_item_1line,
                    it
                )
                searchAutoComplete.setAdapter(arrayAdapter)
            }
        }

        // we will write function to open location in the settings if location
        // is turned off

        val locationRepo = LocationRepositoryImpl()
        locationRepo.getUserLocationCoordinates(
            requireContext(),
            null,
            requestInterval = Long.MAX_VALUE,
            fastestInterval = Long.MAX_VALUE,
            onLocationFetched = {

                mapFragment.updateLocation(it) {
                    finalLocation =  "${it.latitude}/${it.longitude}"
                }

                locateButton.setOnClickListener { v ->
                    val location = searchAutoComplete.text?.toString()
                    val geo = Geocoder(requireContext())
                    val searchLocations = geo.getFromLocationName(location, 1)
                    if (searchLocations.size == 0) {
                        Toast.makeText(requireContext(), "No Such Place Found!", Toast.LENGTH_SHORT).show()
                    }else {
                        val searchL =
                            LatLng(searchLocations[0].latitude, searchLocations[0].longitude)

                        finalLocation = "${searchL.latitude}/${searchL.longitude}"
                        mapFragment.updateLocation(it, searchL) {
                            // when another location is selected
                            finalLocation = "${it.latitude}/${it.longitude}"
                        }
                    }
                }

            }) {
            // here distance is fetched but since target is null no distance is fetched
        }
    }

    override fun onStart() {
        super.onStart()
        finalLocation = "default"
    }

    override fun onResume() {
        super.onResume()
        finalLocation = "default"
    }

}