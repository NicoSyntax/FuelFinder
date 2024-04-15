package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.databinding.FragmentSettingsBinding
import de.syntax.androidabschluss.viewmodel.FirebaseViewModel
import de.syntax.androidabschluss.viewmodel.MainViewModel

class SettingsFragment() : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val firebaseViewModel: FirebaseViewModel by activityViewModels()
    private val viewModel: MainViewModel by activityViewModels()


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

        val spinnerItems= listOf(" - ", "E5", "E10", "Diesel")

        val seekBar = binding.radSlider

        binding.dropdownMenu.setBackgroundColor(resources.getColor(R.color.white))

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.dropdownMenu.adapter = arrayAdapter

        binding.dropdownMenu.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem != " - "){
                    viewModel.selectedSpinnerItem.value = selectedItem
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.sliderValue.text = progress.toString() + " KM"

                if (progress != 4){
                    viewModel.rad = progress
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        binding.logoutBtn.setOnClickListener {
            firebaseViewModel.logout()
            findNavController().navigate(R.id.loginFragment)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.dropdownMenu.onSaveInstanceState()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.dropdownMenu.selectedItem
    }
}