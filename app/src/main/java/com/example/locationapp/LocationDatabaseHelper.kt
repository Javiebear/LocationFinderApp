package com.example.locationapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocationDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ){

    // setting up the constant values
    companion object{
        private const val DATABASE_NAME = "location.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "locations"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LONG = "longitude"
        private const val COLUMN_LAT = "latitude"
    }

    // this function creates the tables of the database
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_ADDRESS TEXT, $COLUMN_LONG TEXT, $COLUMN_LAT TEXT)"
        db?.execSQL(createTableQuery)
    }

    // updates a table
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    // this function is to inset nodes into the database
    fun insertLocation(location: Location){
        val db = writableDatabase
        val values = ContentValues().apply{ // Content values is used to store values associated with column names
            put(COLUMN_ADDRESS, location.address) // apply allows for operations to occur and put allows you to add to the database
            put(COLUMN_LONG, location.longitude)
            put(COLUMN_LAT, location.latitude)

        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    // this function is used to query all the elements in the database
    fun getAllLocations(): List<Location>{
        val locationList = mutableListOf<Location>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME" //getting all values from the table
        val cursor = db.rawQuery(query, null) // result is stored in the cursor

        while(cursor.moveToNext()) { // we iterate through the cursor to see the results
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            val longitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LONG))
            val latitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAT))


            val location = Location(id, address, longitude, latitude)
            locationList.add(location)
        }
        cursor.close()
        db.close()
        return locationList

    }

    // this function is used to query items that is searched in the search bar
    fun getQueriedLocations(search: String): List<Location>{
        val locationList = mutableListOf<Location>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ADDRESS LIKE ?"
        val cursor = db.rawQuery(query, arrayOf("%$search%"))

        while(cursor.moveToNext()) { // we iterate through the cursor to see the results
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            val longitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LONG))
            val latitude = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAT))


            val location = Location(id, address, longitude, latitude)
            locationList.add(location)
        }
        cursor.close()
        db.close()
        return locationList
    }

    // this function will delete a specified item within the database
    fun deleteItem(id: Int){
        val db = this.writableDatabase

        val clause = "id = ?"
        val noteID = arrayOf(id.toString())

        // deleting the item with this ID
        db.delete("locations", clause, noteID)

        db.close()
    }

    //this function will edit a specified item within the database
    fun updateLocation( id : Int, address: String, longitude: String, latitude: String){
        val db = this.writableDatabase //setting up to write to the database

        // storing all the values associated with their column names
        val values = ContentValues().apply {
            put(COLUMN_ADDRESS, address)
            put(COLUMN_LONG, longitude)
            put(COLUMN_LAT, latitude)

        }

        // updating the note based on the id passed in
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())

        // updating the note
        db.update(TABLE_NAME, values, selection, selectionArgs)

        db.close()
    }

}
