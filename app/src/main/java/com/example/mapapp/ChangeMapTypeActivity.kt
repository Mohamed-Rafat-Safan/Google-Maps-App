package com.example.mapapp

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

class ChangeMapTypeActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var myMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_map_type)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            findViewById<View>(R.id.view).visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            findViewById<View>(R.id.view).visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        }

        // Set Custom Action Bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        // Load the map fragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapsType) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        // Move camera on cairo
        val myLocation = LatLng(30.0444, 31.2357)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))  // zoom on my location

        val options = MarkerOptions().position(myLocation).title("Cairo")
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))  // to change mark color in map
        myMap.addMarker(options)


        // To Enable Zoom & Compass Controls on Google Maps
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.uiSettings.isCompassEnabled = true

        // to stop zoom my hand and scroll
//        myMap.uiSettings.isZoomGesturesEnabled = false
//        myMap.uiSettings.isScrollGesturesEnabled = false
    }


    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_map_types, menu)
        return true
    }


    // Handle menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, "map_normal", Toast.LENGTH_SHORT).show()
        when (item.itemId) {
            R.id.map_none -> {
                myMap.mapType = GoogleMap.MAP_TYPE_NONE
            }

            R.id.map_normal -> {
                myMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                Toast.makeText(this, "map_normal", Toast.LENGTH_SHORT).show()
            }

            R.id.map_satellite -> {
                myMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }

            R.id.map_terrain -> {
                myMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            }

            R.id.map_hybrid -> {
                myMap.mapType = GoogleMap.MAP_TYPE_HYBRID
            }

            else -> super.onOptionsItemSelected(item)
        }

        return true // Return true after handling the menu selection
    }


}


