package com.example.disasteralert.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.disasteralert.databinding.FilterItemBinding

class FilterAdapter(private val filterList: List<String>) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.ViewHolder {
        val binding = FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterAdapter.ViewHolder, position: Int) {
        val filterItem = filterList.get(position)
        holder.bind(filterItem)
    }

    override fun getItemCount(): Int = filterList.size

    class ViewHolder(var binding: FilterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filterItem: String) {
            binding.tvFilterName.text = filterItem
        }
    }
}