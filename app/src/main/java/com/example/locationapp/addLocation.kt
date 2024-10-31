package com.example.locationapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.locationapp.databinding.ActivityAddLocationBinding

class addLocation : AppCompatActivity() {

    private lateinit var binding: ActivityAddLocationBinding
    private lateinit var db: LocationDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initializing binding for different view in the layout
        binding = ActivityAddLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing the database
        db = LocationDatabaseHelper(this)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // handling the buttons
        binding.doneButton.setOnClickListener {
            handleDone(it)
        }

        binding.backButton.setOnClickListener {
            handleBack(it)
        }
    }

    // this will send the user back if they do not want to add a location
    fun handleBack(view: View?){
        finish()
    }

    // this will send the user back and insert the new data into the database
    fun handleDone(view: View?){

        val address = binding.addressEditText.text.toString()
        val longitude = binding.longitudeEditText.text.toString()
        val latitude = binding.latitudeEditText.text.toString()

        // check if everything is filled in
        if(title != "" && address != "" && longitude != "" && latitude != ""){
            val location = Location(0, address, longitude, latitude)
            db.insertLocation(location)

            Toast.makeText(this, "Location Added", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(applicationContext, "Failed to add your location!\nYou are missing some entries!", Toast.LENGTH_LONG).show()
        }
        finish()
    }
}