package de.syntax.androidabschluss.ui

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.data.Repository
import de.syntax.androidabschluss.data.remote.FuelFinderApi
import de.syntax.androidabschluss.databinding.FragmentSettingsBinding
import de.syntax.androidabschluss.local.getDatabase
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel

class SettingsFragment(application: Application) : Fragment() {

    private val database = getDatabase(application)
    val repository = Repository(FuelFinderApi, database)
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: FirebaseViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seekBar = binding.radSlider
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.sliderValue.text = progress.toString()
                repository.rad = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.loginFragment)
        }

    }
}


