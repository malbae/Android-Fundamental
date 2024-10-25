package com.example.eventdicoding.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventdicoding.data.response.ListEventsItem
import com.example.eventdicoding.databinding.ItemEventGridBinding
import com.example.eventdicoding.databinding.ItemEventVerticalBinding

class EventAdapter(
    private val layoutType: LayoutType = LayoutType.Vertical, // Enum for layout type
    private val onItemClick: (ListEventsItem) -> Unit // Callback for item click
) : ListAdapter<ListEventsItem, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    // ViewHolder for Horizontal layout
    inner class HorizontalViewHolder(private val binding: ItemEventVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            Glide.with(binding.ivEventImage.context).load(event.imageLogo).into(binding.ivEventImage)
            binding.root.setOnClickListener {
                onItemClick(event) // Only handle the click event
            }
        }
    }

    // ViewHolder for Grid layout
    inner class GridViewHolder(private val binding: ItemEventGridBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            Glide.with(binding.ivEventImage.context).load(event.imageLogo).into(binding.ivEventImage)
            binding.root.setOnClickListener {
                onItemClick(event) // Only handle the click event
            }
        }
    }

    // ViewHolder for Vertical layout (Default)
    inner class VerticalViewHolder(private val binding: ItemEventVerticalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.tvEventName.text = event.name
            binding.tvEventTime.text = event.beginTime
            Glide.with(binding.ivEventImage.context).load(event.imageLogo).into(binding.ivEventImage)
            binding.root.setOnClickListener {
                onItemClick(event) // Only handle the click event
            }
        }
    }

    // Enum for layout type
    enum class LayoutType {
        Vertical, Horizontal, Grid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (layoutType) {
            LayoutType.Horizontal -> HorizontalViewHolder(
                ItemEventVerticalBinding.inflate(inflater, parent, false)
            )
            LayoutType.Grid -> GridViewHolder(
                ItemEventGridBinding.inflate(inflater, parent, false)
            )
            LayoutType.Vertical -> VerticalViewHolder(
                ItemEventVerticalBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)
        when (holder) {
            is HorizontalViewHolder -> holder.bind(event)
            is GridViewHolder -> holder.bind(event)
            is VerticalViewHolder -> holder.bind(event)
        }
    }
}
