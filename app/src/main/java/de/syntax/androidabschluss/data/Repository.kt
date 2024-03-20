package de.syntax.androidabschluss.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.syntax.androidabschluss.data.model.ApiResponse
import de.syntax.androidabschluss.data.remote.FuelFinderApi


class Repository(private val api: FuelFinderApi) {
    private val TAG = "Repository"

    private val key = "eaefa201-66ec-a001-e4ee-8a91dfdcc983"

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
            val stations = api.retrofitService.getStationsWithLocation(lat, lng, 4, key)
            _stationList.value = stations
        } catch (e: Exception){
            Log.e(TAG, "Error loading data with Location from Repository: $e")
        }
    }
}