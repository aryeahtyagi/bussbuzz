package com.fortradestudio.busbuzz

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fortradestudio.busbuzz.location.LocationRepositoryImpl
import com.fortradestudio.busbuzz.service.ReminderService
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.fortradestudio.busbuzz.location.Location as CustomLocation


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stopService(Intent(applicationContext, ReminderService::class.java))

        val locationRepo = LocationRepositoryImpl()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onCreate: not granted")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==100 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            // here permission is  not granted so we request again
             MaterialAlertDialogBuilder(this)
                 .setCancelable(false)
                 .setMessage("Location Permission is Required !!")
                 .setTitle("Permission not granted")
                 .setPositiveButton("Request Again") { dialog, which ->
                     ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
                 }
                 .setNegativeButton("Exit App"){dialog , which->
                     finish()
                 }
                 .show()
        }

    }


}