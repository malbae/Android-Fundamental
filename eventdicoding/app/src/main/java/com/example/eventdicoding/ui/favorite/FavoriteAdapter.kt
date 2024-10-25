package com.example.eventdicoding.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eventdicoding.R
import com.example.eventdicoding.database.local.entity.FavoriteUser
import com.example.eventdicoding.databinding.ItemFavoriteEventBinding
import androidx.core.text.HtmlCompat

class FavoriteAdapter(private var events: List<FavoriteUser>, private val onClick: (FavoriteUser) -> Unit) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(private val binding: ItemFavoriteEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteUser) {
            // Set the event title
            binding.textTitle.text = event.login // Using login as title for now

            // Set the event time (formatted)
            binding.textTime.text = event.beginTime ?: "Time not available"

            // Load the event image using Glide
            Glide.with(binding.root.context)
                .load(event.avatar_url) // Use avatar_url for the image
                .placeholder(R.drawable.ic_placeholder) // Placeholder while loading
                .error(R.drawable.error_image) // Error image if loading fails
                .into(binding.imageEvent)

            // Handle item click
            binding.root.setOnClickListener { onClick(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    // Update the list of events and notify the adapter
    fun updateData(newEvents: List<FavoriteUser>) {
        events = newEvents
        notifyDataSetChanged()
    }
}