package com.example.mapapp


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mapapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            findViewById<View>(R.id.view).visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            findViewById<View>(R.id.view).visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        }


        binding.btnCurrentLocation.setOnClickListener {
            startActivity(Intent(this, CurrentLocationActivity::class.java))
        }

        binding.btnSearchLocation.setOnClickListener {
            startActivity(Intent(this, SearchLocationActivity::class.java))
        }

        binding.btnChangeMapType.setOnClickListener {
            startActivity(Intent(this, ChangeMapTypeActivity::class.java))
        }

        binding.btnDisplayTrackOnMap.setOnClickListener {
            startActivity(Intent(this, DisplayTrackOnMapActivity::class.java))
        }


    }

}