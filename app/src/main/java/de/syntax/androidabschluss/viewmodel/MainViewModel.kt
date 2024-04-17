package de.syntax.androidabschluss.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.data.Repository
import de.syntax.androidabschluss.data.local.getDatabase
import de.syntax.androidabschluss.data.model.Station
import de.syntax.androidabschluss.data.remote.FuelFinderApi
import kotlinx.coroutines.launch

class MainViewModel (application: Application): AndroidViewModel(application){
    private val TAG = "MainViewModel"

    private val database = getDatabase(application)
    private val repository = Repository(FuelFinderApi, database)

    var rad = 4

    var stations = repository.stationList

    val favorites = repository.favorites

    val selectedSpinnerItem = repository.selectedSpinnerItem

    var spinnerPosition = 0

    fun loadStationsWithLocation(lat: Double, lng: Double){
        viewModelScope.launch {
            repository.getStationsWithLocation(lat, lng, rad)
        }
    }

    fun insertStation(station: Station) {
        viewModelScope.launch {
            repository.insertStation(station)
        }
    }
    fun deleteStation(station: Station) {
        viewModelScope.launch {
            repository.deleteStation(station)
        }
    }

    fun navigateWithGoogle(context: Context, address: String) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$address")

        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        mapIntent.setPackage("com.google.android.apps.maps")

        context.startActivity(mapIntent)
    }
}

