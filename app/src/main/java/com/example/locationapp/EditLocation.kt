package com.example.locationapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.locationapp.databinding.ActivityEditLocationBinding
import android.view.View
import android.widget.Toast




class EditLocation : AppCompatActivity() {
    private lateinit var binding: ActivityEditLocationBinding
    private lateinit var db: LocationDatabaseHelper
    var id:Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initializing binding for different view in the layout
        binding = ActivityEditLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing the database
        db = LocationDatabaseHelper(this)

        // setting up the values so that they properly display what the user selected
        id = intent.getIntExtra("EXTRA_ID", 0)
        binding.addressEditText.setText(intent.getStringExtra("EXTRA_ADDRESS"))
        binding.longitudeEditText.setText(intent.getStringExtra("EXTRA_LONGITUDE"))
        binding.latitudeEditText.setText(intent.getStringExtra("EXTRA_LATITUDE"))

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

        binding.deleteButton.setOnClickListener {
            handleDeleteNote()
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
            db.updateLocation(id, address, longitude, latitude)

            Toast.makeText(this, "Location Edited", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            Toast.makeText(applicationContext, "Failed to add your location!\nYou are missing some entries!", Toast.LENGTH_LONG).show()
        }
        finish()
    }

    // this function will delete the location item in the database
    fun handleDeleteNote(){
        db.deleteItem(id)
        finish()
    }}