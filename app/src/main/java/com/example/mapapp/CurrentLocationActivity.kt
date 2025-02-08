package com.example.mapapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class CurrentLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private val FINE_PERMISSION_CODE = 1
    private lateinit var myMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            findViewById<View>(R.id.view).visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            findViewById<View>(R.id.view).visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        }

        // Initialize location provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getCurrentLocation()
    }


    private fun getCurrentLocation() {
        // He will check whether he has permission for the current location or not.,, if take he exit from function
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // this if click on allow to get current location will send my permission come
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), FINE_PERMISSION_CODE)
            return
        }

        // If I give him permission, he will take the current location.
        val task: Task<Location> = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location

                // Load the map fragment
                val mapFragment = supportFragmentManager.findFragmentById(R.id.currentLocationMap) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }


    override fun onMapReady(googleMpa: GoogleMap) {
        myMap = googleMpa

        // this put mark on my location
        val myLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15f))  // zoom on my location

        val options = MarkerOptions().position(myLocation).title("My Location")
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))  // to change mark color in map
        myMap.addMarker(options)

        // To Enable Zoom & Compass Controls on Google Maps
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.uiSettings.isCompassEnabled = true

        // to stop zoom my hand and scroll
//        myMap.uiSettings.isZoomGesturesEnabled = false
//        myMap.uiSettings.isScrollGesturesEnabled = false
    }



    // Handle permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        }else{
            Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show()
        }
    }

}