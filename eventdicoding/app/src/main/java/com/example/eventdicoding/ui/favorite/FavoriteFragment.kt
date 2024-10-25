package com.example.eventdicoding.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eventdicoding.R
import com.example.eventdicoding.data.response.ListEventsItem
import com.example.eventdicoding.database.room.FavoriteUserRoomDatabase
import com.example.eventdicoding.database.room.FavoriteUserDao
import com.example.eventdicoding.database.local.entity.FavoriteUser
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteUserDao: FavoriteUserDao
    private lateinit var emptyFavoritesTextView: TextView
    private var favoriteUsers: List<FavoriteUser> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_favorite_events)
        emptyFavoritesTextView = view.findViewById(R.id.tv_empty_message)

        // Inisialisasi DAO
        val database = FavoriteUserRoomDatabase.getDatabase(requireContext())
        favoriteUserDao = database.favoriteUserDao()

        // Inisialisasi adapter dengan klik listener untuk navigasi
        favoriteAdapter = FavoriteAdapter(favoriteUsers) { favoriteUser ->
            // Panggil fungsi untuk navigasi ke halaman detail event
            navigateToDetail(favoriteUser)
        }

        // Inisialisasi adapter dan set ke RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = favoriteAdapter

        loadFavorites()
    }

    private fun loadFavorites() {
        lifecycleScope.launch {
            favoriteUserDao.getFavoriteUser().observe(viewLifecycleOwner) { users ->
                favoriteUsers = users
                if (favoriteUsers.isEmpty()) {
                    emptyFavoritesTextView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyFavoritesTextView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    favoriteAdapter.updateData(favoriteUsers) // Memperbarui data di adapter
                }
            }
        }
    }

    // Fungsi untuk navigasi ke halaman detail
    private fun navigateToDetail(favoriteUser: FavoriteUser) {
        val listEventsItem = favoriteUser.toListEventsItem() // Konversi ke ListEventsItem
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(listEventsItem)
        findNavController().navigate(action)
    }
}

// Fungsi ekstensi untuk konversi dari FavoriteUser ke ListEventsItem
fun FavoriteUser.toListEventsItem(): ListEventsItem {
    return ListEventsItem(
        id = this.id,
        name = this.login, // Maps to event name
        mediaCover = this.avatar_url, // Maps to media cover image
        description = this.description, // Maps to event description
        cityName = this.eventLocation, // Maps to event location
        beginTime = this.beginTime, // Maps to event start time
        endTime = this.endTime, // Maps to event end time
        quota = this.quota, // Maps to event quota
        registrants = this.registrants // Maps to current number of registrants
    )
}

