package de.syntax.androidabschluss.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.syntax.androidabschluss.data.Repository
import de.syntax.androidabschluss.data.remote.FuelFinderApi
import kotlinx.coroutines.launch

class MainViewModel (application: Application): AndroidViewModel(application){
    private val TAG = "MainViewModel"

    private val repository = Repository(FuelFinderApi)

    val stations = repository.stationList

    init {
        loadStations()
    }

    fun loadStations(){
        try {
            viewModelScope.launch {
                repository.getStations()
            }
        } catch (e: Exception){
            Log.e(TAG, "error loading data: $e")
        }
    }

}

