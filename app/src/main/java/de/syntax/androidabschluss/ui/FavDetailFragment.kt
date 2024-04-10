package de.syntax.androidabschluss.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
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

        val station = viewModel.favorites.value?.find { stationId == it.id }

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

        binding.removeFv.setOnClickListener {
            if (station != null) {
                viewModel.deleteStation(station)
            }
            station?.isFavourite = false
            Toast.makeText(context, "aus Favoriten entfernt", Toast.LENGTH_LONG).show()
        }
    }
}

fun openGoogleMapsNavigation(context: Context, address: String) {
    val gmmIntentUri = Uri.parse("google.navigation:q=$address")

    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

    mapIntent.setPackage("com.google.android.apps.maps")

    if (mapIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(mapIntent)
    } else {
        Toast.makeText(context, "Google maps wurde auf Ihrem Gerät nicht gefunden",Toast.LENGTH_LONG).show()
    }
}