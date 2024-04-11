package de.syntax.androidabschluss.ui


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
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

        mainViewModel.stations.observe(viewLifecycleOwner){
            binding.rvStations.adapter = HomeAdapter(it.stations)
        }
        binding.rvStations.setHasFixedSize(true)

        firebaseViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().navigate(R.id.loginFragment)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(locationListener)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            Toast.makeText(requireContext(),
                "Location permission denied",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun requestLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_FOR_UPDATE,
                MIN_DISTANCE_CHANGE,
                locationListener
            )
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(),
                "Location permission not granted",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        private const val MIN_TIME_FOR_UPDATE: Long = 1000 * 60 * 1
        private const val MIN_DISTANCE_CHANGE: Float = 10f
    }
}
