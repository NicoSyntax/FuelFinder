package de.syntax.androidabschluss.ui


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.HomeAdapter
import de.syntax.androidabschluss.databinding.FragmentHomeBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel
import de.syntax.androidabschluss.viewmodel.MainViewModel

class HomeFragment : Fragment() {

    private lateinit var locationManager: LocationManager

    private val mainViewModel: MainViewModel by activityViewModels()
    private val firebaseViewModel: FirebaseViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    //location listener to get current location from device and start api call with that location
    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

            val latitude = location.latitude
            val longitude = location.longitude
            mainViewModel.loadStationsWithLocation(latitude, longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }

    private val permissionRequestCode = 1

    //get gps permission from user and load stations with location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationUpdates()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permissionRequestCode
            )
        }
        getCurrentLocationAndLoadStations()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //sort list depending on user settings
        mainViewModel.stations.observe(viewLifecycleOwner) { apiResponse ->

            when (mainViewModel.selectedSpinnerItem.value) {
                "Diesel" -> mainViewModel.stations.value?.stations =
                    mainViewModel.stations.value?.stations?.sortedBy { it.diesel }!!

                "E5" -> mainViewModel.stations.value?.stations =
                    mainViewModel.stations.value?.stations?.sortedBy { it.e5 }!!

                "E10" -> {
                    mainViewModel.stations.value?.stations =
                        mainViewModel.stations.value?.stations?.sortedBy { it.e10 }!!

                    mainViewModel.stations.value?.stations?.filter { it.e10 != null }.toString()
                }
            }

            //if api call failed make no stations found TV visible and set adapter
            if (mainViewModel.stations.value?.stations?.isEmpty() == true) {
                binding.tvNoStation.visibility = View.VISIBLE
            } else {
                mainViewModel.selectedSpinnerItem.value?.let { Log.e("HomeFragment", it) }
                binding.tvNoStation.visibility = View.GONE
                binding.rvStations.adapter = HomeAdapter(apiResponse.stations)
            }
        }
        //set fixed size for performance
        binding.rvStations.setHasFixedSize(true)

        //if no user logged in navigate to LoginFragment
        firebaseViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        }

        //refresh btn to make new apicall with device location
        binding.refreshBtn.setOnClickListener {
            getCurrentLocationAndLoadStations()
        }

    }

    //remove location updates onDestroy
    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    //handles permission granted and denies
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            Toast.makeText(
                requireContext(),
                "Location permission denied",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    //update location from device
    private fun requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_FOR_UPDATE,
                MIN_DISTANCE_CHANGE,
                locationListener
            )
        } catch (e: SecurityException) {
            Toast.makeText(
                requireContext(),
                "Location permission not granted",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    //settings for location updates (Time and Distance)
    companion object {
        private const val MIN_TIME_FOR_UPDATE: Long = 1000 * 60
        private const val MIN_DISTANCE_CHANGE: Float = 10f
    }

    //get Location from device and send APICall
    private fun getCurrentLocationAndLoadStations() {
        try {
            val lastKnownLocation =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastKnownLocation?.let {

                val latitude = it.latitude
                val longitude = it.longitude
                mainViewModel.loadStationsWithLocation(latitude, longitude)
            }
        } catch (e: SecurityException) {
            Toast.makeText(
                requireContext(),
                "Location permission not granted",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

}
