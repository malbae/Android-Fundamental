package com.example.eventdicoding.ui.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventdicoding.database.room.FavoriteUserRoomDatabase
import com.example.eventdicoding.database.local.entity.FavoriteUser
import com.example.eventdicoding.database.room.FavoriteUserDao
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : ViewModel() {
    private val favoriteUserDao: FavoriteUserDao? // Menggunakan nullable untuk DAO

    init {
        val database = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteUserDao = database?.favoriteUserDao() // Inisialisasi DAO dengan pemeriksaan null
        if (favoriteUserDao == null) {
            Log.e("FavoriteViewModel", "Failed to initialize FavoriteUserDao")
            // Tampilkan pesan kesalahan di UI jika diperlukan
        }
    }

    // LiveData untuk mendapatkan daftar favorite users
    val favoriteUsers: LiveData<List<FavoriteUser>>? = favoriteUserDao?.getFavoriteUser() // Memastikan LiveData juga nullable

    fun addFavoriteUser(user: FavoriteUser) {
        viewModelScope.launch {
            favoriteUserDao?.addToFavorite(user) // Menggunakan metode addToFavorite jika DAO tidak null
        }
    }

    fun removeFavoriteUser(user: FavoriteUser) {
        viewModelScope.launch {
            favoriteUserDao?.removeFromFavorite(user.id) // Menggunakan id untuk menghapus jika DAO tidak null
        }
    }
}