package com.example.disasteralert.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.disasteralert.R
import com.example.disasteralert.databinding.FilterItemBinding

class FilterAdapter(
    private val onDisasterFilterClick: (String) -> Unit,
    private val onDisasterDrawable: (String, ImageView) -> Unit
) : ListAdapter<String, FilterAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filterItem = getItem(position)
        holder.bind(filterItem)

        holder.binding.cvFilterItem.setOnClickListener {
            onDisasterFilterClick(filterItem)
            onDisasterDrawable(filterItem, holder.binding.ivFilterStatus)
        }
    }

    class ViewHolder(var binding: FilterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(filterItem: String) {
            binding.tvFilterName.text = filterItem
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<String> =
            object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem.length == newItem.length
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }
            }
    }
}