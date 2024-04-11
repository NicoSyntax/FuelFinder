package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.FavoriteAdapter
import de.syntax.androidabschluss.databinding.FragmentFavoriteBinding
import de.syntax.androidabschluss.viewmodel.MainViewModel


class FavoriteFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favorites.observe(viewLifecycleOwner){
            if (viewModel.favorites.value?.isEmpty() == true){
                binding.emptyFav.setText(R.string.sie_haben_noch_keine_favoriten_hinzugef_gt)
            } else  {
                binding.rvStations.adapter = FavoriteAdapter(it)
                binding.emptyFav.text = " "
            }
        }
        binding.rvStations.setHasFixedSize(true)

    }
}