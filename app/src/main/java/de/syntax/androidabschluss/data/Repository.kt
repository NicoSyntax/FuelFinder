package de.syntax.androidabschluss.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.syntax.androidabschluss.data.model.ApiResponse
import de.syntax.androidabschluss.data.model.Station
import de.syntax.androidabschluss.data.remote.FuelFinderApi
import de.syntax.androidabschluss.local.StationDatabase


class Repository(private val api: FuelFinderApi, private val database: StationDatabase) {
    private val TAG = "Repository"

    private val key = "eaefa201-66ec-a001-e4ee-8a91dfdcc983"

    var rad = 4

    private val _stationList = MutableLiveData<ApiResponse>()
    val stationList: LiveData<ApiResponse>
        get() = _stationList

    suspend fun getStations() {
        try {
            val stations = api.retrofitService.getStations(key)
            _stationList.value = stations
        } catch (e: Exception) {
            Log.e(TAG, "Error loading Data from Repository $e")
        }
    }

    suspend fun getStationsWithLocation(lat: Double, lng: Double){
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
            Log.e(TAG, "Error inserting Note from Database e: $e")
        }
    }

    suspend fun deleteStation(station: Station) {
        try {
            database.FuelFinderDatabaseDao.deleteStation(station)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting Note from Database e: $e")
        }
    }
}