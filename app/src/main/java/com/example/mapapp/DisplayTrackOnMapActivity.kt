package com.example.mapapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mapapp.databinding.ActivityDisplayTrackOnMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

class DisplayTrackOnMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayTrackOnMapBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayTrackOnMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) { // API 30 corresponds to Android 11 (R)
            // Code for API levels above 30
            findViewById<View>(R.id.view).visibility = View.VISIBLE
        } else {
            // Code for API 30 and below
            findViewById<View>(R.id.view).visibility = View.GONE
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar)
        }

        // فكرة الكود انه هيدخل مكانك والمكان الي انت هتتجه اليه وبعد كده هياخدهم ويروح
        // يفتح برنامج جوجل ماب ولو البرنامج ليس موجود علي جهازك هيبعتك الي المتجر بلاي لكي تنصب التطبيق

        binding.btnDirection.setOnClickListener {
            val yourLocation = binding.etYourLocation.text.toString()
            val destination = binding.etDestination.text.toString()
            if (yourLocation.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "Please enter your location & destination", Toast.LENGTH_SHORT)
                    .show()
            } else {
                getDirections(yourLocation, destination)
            }
        }

    }


    private fun getDirections(from: String, to: String) {
        try {
            val uri = Uri.parse("https://www.google.com/maps/dir/$from/$to")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val uri =
                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            Log.i("Error", e.message.toString())
        }
    }

}