package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import de.syntax.androidabschluss.databinding.FragmentHomeDetailBinding
import de.syntax.androidabschluss.viewmodel.MainViewModel


class HomeDetailFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeDetailBinding
    private var stationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            stationId = it.getString("StationId")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val station = viewModel.stations.value?.stations?.find { stationId == it.id }

        if (station?.e10 == null){
            binding.e10Price.text = "/"
        } else {
            binding.e10Price.text = station.e10.toString()
        }
        binding.e10Price.text = station?.e10.toString() + "€"
        binding.e5Price.text = station?.e5.toString() + "€"
        binding.dieselPrice.text = station?.diesel.toString() + "€"
        binding.stationAddress.text = station?.street + " " + station?.houseNumber
        binding.brandStation.text = station?.brand
        binding.backButton.setOnClickListener {
            val navController = binding.backButton.findNavController()
            navController.navigateUp()
        }

        binding.fvBtn.setOnClickListener {
            if (station != null) {
                viewModel.insertStation(station)
            }
            station?.isFavourite = true
            Toast.makeText(context, "zu Favoriten hinzugefügt", Toast.LENGTH_LONG).show()
        }

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