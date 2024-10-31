package com.example.locationapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter(
    private var locationList: List<Location>,
    private val context: Context,
    private val itemClickDeleteListener: (Location) -> Unit,
    ) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>(){

    interface OnItemClickListener {
        fun handleLocationClicked(location: Location)
    }

    // assigning all the values of the Property data object
    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val address: TextView = itemView.findViewById(R.id.addressTextView)
        val longitude: TextView = itemView.findViewById(R.id.longitudeTextView)
        val latitude: TextView = itemView.findViewById(R.id.latitudeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.locationitem, parent, false)
        return LocationViewHolder(view)
    }

    // returns the size of the list
    override fun getItemCount(): Int = locationList.size

    // this method assigns all the values to each note
    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList[position]

        holder.address.text = location.address
        holder.longitude.text = "Longitude: " + location.longitude
        holder.latitude.text = "Latitude: " + location.latitude

        // making the function to handle if the item has been clicked
        holder.itemView.setOnClickListener(){
            // allow the user to view it and add new activity/layout
            itemClickDeleteListener(location)
        }

    }

    // this method is to update the data when a new item is added to the database
    fun refreshData(locationList: List<Location>){
        this.locationList = locationList
        notifyDataSetChanged()
    }
}