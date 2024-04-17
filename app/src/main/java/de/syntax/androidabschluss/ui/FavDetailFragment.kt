package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import de.syntax.androidabschluss.databinding.FragmentFavDetailBinding
import de.syntax.androidabschluss.viewmodel.MainViewModel


class FavDetailFragment : Fragment() {

    val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentFavDetailBinding
    private var stationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stationId = it.getString("stationId")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //find right station by ID
        val station = viewModel.favorites.value?.find { stationId == it.id }


        if (station?.e10 == null) {
            binding.e10Price.text = "/"
        } else {
            binding.e10Price.text = station.e10.toString()
        }

        //set data for detail fragment into TV
        binding.e10Price.text = station?.e10.toString() + "€"
        binding.e5Price.text = station?.e5.toString() + "€"
        binding.dieselPrice.text = station?.diesel.toString() + "€"
        binding.stationAddress.text = station?.street + " " + station?.houseNumber
        binding.brandStation.text = station?.brand

        //navigate back
        binding.backButton.setOnClickListener {
            val navController = binding.backButton.findNavController()
            navController.navigateUp()
        }

        //delete station from Database
        binding.removeFv.setOnClickListener {
            if (station != null) {
                viewModel.deleteStation(station)
            }
            station?.isFavourite = false
            Toast.makeText(context, "aus Favoriten entfernt", Toast.LENGTH_LONG).show()
        }


        //navigate btn to start google intent with address from station
        binding.navigateBtn.setOnClickListener {
            if (station != null) {
                viewModel.navigateWithGoogle(
                    requireContext(),
                    station.street + station.houseNumber + " " + station.place + " " + station.postCode
                )
            }
        }
    }
}

