package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.data.model.Station
import de.syntax.androidabschluss.databinding.StationItemBinding
import de.syntax.androidabschluss.ui.HomeFragmentDirections

class HomeAdapter(val dataset:List<Station>) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(val binding: StationItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = StationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val station = dataset[position]
        holder.binding.stationBrandTv.text = station.brand
        holder.binding.dieselPrice.text = station.diesel.toString()
        holder.binding.e5Price.text = station.e5.toString()
        holder.binding.e10Price.text = station.e10.toString()

        if (!station.isOpen){
            holder.binding.stationItem.alpha = 0.5f
        }

        holder.binding.stationItem.setOnClickListener {
            val navController = holder.binding.stationItem.findNavController()
            navController.navigate(
                HomeFragmentDirections.actionHomeFragmentToHomeDetailFragment(station.id))
        }
    }
}