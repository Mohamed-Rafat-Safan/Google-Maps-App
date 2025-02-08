package com.example.mapapp

import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mapapp.databinding.ActivitySearchLocationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException


class SearchLocationActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivitySearchLocationBinding
    private lateinit var myMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivitySearchLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            findViewById<View>(R.id.view).visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            findViewById<View>(R.id.view).visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        }

        setSupportActionBar(binding.toolbarSearchLocation)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search_location, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search_location)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = getString(R.string.search)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location = searchView.query.toString()
                var addressList: List<Address>? = null

                if (location.isNotEmpty()) {
                    val geoCoder = Geocoder(this@SearchLocationActivity)

                    try {
                        addressList = geoCoder.getFromLocationName(location, 1)
                        if (addressList.isNullOrEmpty()) {
                            Toast.makeText(applicationContext, "Location not found. Try again.", Toast.LENGTH_SHORT).show()
                            return false
                        }

                        val address: Address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)

                        Log.i("Address", "Found location: ${address.latitude}, ${address.longitude}")

                        handelLocationInMap(latLng)

                    } catch (e: IOException) {
                        Toast.makeText( applicationContext, "Geocoder service error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                return false
            }

            // Handle text change event
            override fun onQueryTextChange(newText: String?): Boolean { return false }
        })

        // Load the map fragment
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.searchLocationMap) as SupportMapFragment
        mapFragment.getMapAsync(this@SearchLocationActivity)

        return true
    }


    private fun handelLocationInMap(latLng: LatLng) {
        // this put mark on my location
//        val myLocation = LatLng(currentLocation.latitude, currentLocation.longitude)
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))  // zoom on my location

        val options = MarkerOptions().position(latLng).title("My Search Location")
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))  // to change mark color in map
        myMap.addMarker(options)


        // To Enable Zoom & Compass Controls on Google Maps
        myMap.uiSettings.isZoomControlsEnabled = true
        myMap.uiSettings.isCompassEnabled = true

        // to stop zoom my hand and scroll
//        myMap.uiSettings.isZoomGesturesEnabled = false
//        myMap.uiSettings.isScrollGesturesEnabled = false
    }


    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap
    }

}