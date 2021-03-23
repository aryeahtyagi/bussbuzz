package com.fortradestudio.busbuzz

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
import android.location.Geocoder
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.fortradestudio.busbuzz.location.Location
import com.fortradestudio.busbuzz.location.LocationRepositoryImpl
import com.fortradestudio.busbuzz.room.LocationDatabase
import com.fortradestudio.busbuzz.room.Locations
import com.fortradestudio.busbuzz.room.LocationsRepo
import com.fortradestudio.busbuzz.service.ReminderService
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar

class ReminderFragment : Fragment() {

    lateinit var ourLocation: LatLng
    lateinit var locations: Locations
    private val TAG = "ReminderFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationRepository = LocationRepositoryImpl()
        val searched_location = arguments?.getString("location")
        val locationDao = LocationDatabase.getLocationDatabase(requireContext()).locationDao()
        val locationRepo = LocationsRepo(locationDao)

        Log.i(TAG, "onViewCreated: $searched_location")

        val reminderTextView = view.findViewById<TextView>(R.id.targetLocationTextView)
        val distanceEditText = view.findViewById<EditText>(R.id.distanceEditText)
        val reminderButton = view.findViewById<AppCompatButton>(R.id.reminderButton)
        val save = view.findViewById<AppCompatButton>(R.id.save)

        save.setOnClickListener {
            locationRepo.insertToDb(locations)
            Snackbar.make(requireView(), "Location Saved Successfully ✨", Snackbar.LENGTH_SHORT)
                .show()
        }

        distanceEditText.addTextChangedListener {
            reminderButton.isClickable = it.toString().isNotEmpty()
        }

        val location = searched_location?.split("/") as List<String>

        val target = Location(location[0].toDouble(), location[1].toDouble())

        val results =
            Geocoder(requireContext()).getFromLocation(target.latitude, target.longitude, 1);

        if (results.isNotEmpty()) {
            reminderTextView.text = results[0].getAddressLine(0)
            locations = Locations(
                address = results[0].getAddressLine(0),
                latitude = results[0].latitude,
                longitude = results[0].longitude
            )
        }

        reminderButton.setOnClickListener {
            if (distanceEditText.text.toString().isNotEmpty()) {
                val minDis = distanceEditText.text.toString().toInt()
                val intent = Intent(requireContext(), ReminderService::class.java)
                intent.putExtra("target", "${target.latitude}-${target.longitude}")
                intent.putExtra("minDis", minDis)
                ContextCompat.startForegroundService(requireContext().applicationContext, intent)
            }else{
                Toast.makeText(requireContext(),"Select Reminder Distance",Toast.LENGTH_SHORT).show()
            }
        }
    }

}