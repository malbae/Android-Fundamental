package com.example.eventdicoding.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventdicoding.database.local.entity.FavoriteUser
import com.example.eventdicoding.database.room.FavoriteUserDao
import com.example.eventdicoding.data.response.ListEventsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel(private val favoriteUserDao: FavoriteUserDao) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    // Fungsi untuk mengecek apakah event ini sudah ditambahkan ke favorit
    fun checkIfFavorite(eventId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val count = favoriteUserDao.checkUserFavorite(eventId)
            withContext(Dispatchers.Main) {
                _isFavorite.value = count > 0
            }
        }
    }

    // Fungsi untuk menambah atau menghapus favorit
    fun toggleFavorite(event: ListEventsItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val isCurrentlyFavorite = _isFavorite.value ?: false
            if (isCurrentlyFavorite) {
                favoriteUserDao.removeFromFavorite(event.id)
            } else {
                val favoriteUser = FavoriteUser(
                    login = event.name,
                    avatar_url = event.mediaCover,
                    description = event.description,
                    eventLocation = event.cityName,
                    beginTime = event.beginTime,
                    endTime = event.endTime,
                    quota = event.quota,              // Include quota
                    registrants = event.registrants,  // Include registrants
                    id = event.id
                )
                favoriteUserDao.addToFavorite(favoriteUser)
            }

            withContext(Dispatchers.Main) {
                _isFavorite.value = !isCurrentlyFavorite
            }
        }
    }
}
