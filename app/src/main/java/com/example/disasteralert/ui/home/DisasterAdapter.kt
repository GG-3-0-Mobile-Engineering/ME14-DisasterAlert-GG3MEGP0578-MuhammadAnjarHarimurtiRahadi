package com.example.disasteralert.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.disasteralert.R
import com.example.disasteralert.data.local.entity.DisasterEntity
import com.example.disasteralert.data.remote.response.disasterresponse.GeometriesItem
import com.example.disasteralert.databinding.DisasterDetailItemBinding
import com.example.disasteralert.helper.Util

class DisasterAdapter(
    private val onDisasterItemClick: (DisasterEntity) -> Unit
) : ListAdapter<DisasterEntity, DisasterAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DisasterDetailItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val disasterItem = getItem(position)
        holder.bind(disasterItem)

        holder.binding.cvDisasterItem.setOnClickListener {
            onDisasterItemClick(disasterItem)
        }
    }

    class ViewHolder(var binding: DisasterDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(disasterItem: DisasterEntity) {
            binding.apply {
                Glide.with(itemView.context).load(disasterItem.disasterImageUrl).apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error)
                ).into(ivDisasterImage)
                tvDisasterType.text = disasterItem.disasterType
                tvDisasterPlace.text = disasterItem.disasterLoc?.let { Util.getProvinceName(it) }
                tvDisasterTime.text = Util.getDatePresentationFormat(disasterItem.disasterDate)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<DisasterEntity> =
            object : DiffUtil.ItemCallback<DisasterEntity>() {
                override fun areItemsTheSame(
                    oldItem: DisasterEntity, newItem: DisasterEntity
                ): Boolean {
                    return oldItem.pKey == newItem.pKey
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: DisasterEntity, newItem: DisasterEntity
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}