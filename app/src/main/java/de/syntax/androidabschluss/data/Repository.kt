package de.syntax.androidabschluss.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.syntax.androidabschluss.data.local.StationDatabase
import de.syntax.androidabschluss.data.model.ApiResponse
import de.syntax.androidabschluss.data.model.Station
import de.syntax.androidabschluss.data.remote.FuelFinderApi


class Repository(private val api: FuelFinderApi, private val database: StationDatabase) {
    private val TAG = "Repository"

    private val key = "eaefa201-66ec-a001-e4ee-8a91dfdcc983"

    //data for HomeFragment
    private val _stationList = MutableLiveData<ApiResponse>()

    val stationList: LiveData<ApiResponse>
        get() = _stationList

    //data from database
    val favorites: LiveData<List<Station>> = database.FuelFinderDatabaseDao.getAll()

    //currently selected SpinnerItem from Settings
    var selectedSpinnerItem = MutableLiveData<String>()

    suspend fun getStationsWithLocation(lat: Double, lng: Double, rad: Int){
        try {
            val stations = api.retrofitService.getStationsWithLocation(lat, lng, rad, key)
            _stationList.value = stations
            Log.e(TAG, "current value of rad: $rad")
        } catch (e: Exception){
            Log.e(TAG, "Error loading data with Location from Repository: $e")
        }
    }

    suspend fun insertStation(station: Station) {
        try {
            database.FuelFinderDatabaseDao.insertStation(station)
        } catch (e: Exception) {
            Log.e(TAG, "Error inserting Station from Database e: $e")
        }
    }

    suspend fun deleteStation(station: Station) {
        try {
            database.FuelFinderDatabaseDao.deleteStation(station)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting Station from Database e: $e")
        }
    }
}