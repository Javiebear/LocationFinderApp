package com.example.locationapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationapp.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: LocationDatabaseHelper
    private lateinit var locationAdapter: LocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // initializing binding for different view in the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initializing the database
        db = LocationDatabaseHelper(this)

        // populating the database with a file that contains addresses
        // below, each line is read within the file and then is separated by the '/'
        this@MainActivity.assets.open("locations.txt").bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val tokens = line.split("/")
                val location = Location(
                    id = 0,
                    address = tokens[0].trim(),
                    longitude = tokens[1].trim(),
                    latitude = tokens[2].trim()
                )
                db.insertLocation(location)
            }
        }

        // making the recycler view at the top go horizontal
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.LocationRecyclerView.layoutManager = layoutManager

        // getting the database elements and adapting them
        locationAdapter = LocationAdapter(
            db.getAllLocations(),
            this,
            {clickedItem -> handleLocationClicked(clickedItem)})

        // binding the database elements to the recycler view
        binding.LocationRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.LocationRecyclerView.adapter = locationAdapter


        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // binding all the buttons
        binding.addLocationButton.setOnClickListener {
            addLocation(it)
        }

        // binding the search bar to allow users to search for specified elements
        binding.searchAddress.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(search: String?): Boolean {
                search(search)
                return true
            }
        })
    }

    //this function will refresh the data when called
    override fun onResume() {
        super.onResume()
        locationAdapter.refreshData(db.getAllLocations())
    }

    // this will open up a new activity for a user to enter in a location
    fun addLocation(view: View?){
        val intent = Intent(this, addLocation::class.java)
        startActivity(intent)
    }

    // this function will handle the searching of items in the database
    fun search(search: String?){
        if(search.isNullOrEmpty()){
            // if nothing is searched then all the notes are displayed
            locationAdapter.refreshData(db.getAllLocations())

        }
        else
        {
            locationAdapter.refreshData(db.getQueriedLocations(search))
        }

    }

    // this will send to user to an edit activity where they can delete or edit the location
    fun handleLocationClicked(location: Location){
        val intent = Intent(this, EditLocation::class.java)
        intent.putExtra("EXTRA_ID", location.id)
        intent.putExtra("EXTRA_ADDRESS", location.address)
        intent.putExtra("EXTRA_LONGITUDE", location.longitude)
        intent.putExtra("EXTRA_LATITUDE", location.latitude)
        startActivity(intent)
    }
}