package com.example.eventdicoding.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eventdicoding.database.room.FavoriteUserDao
import com.example.eventdicoding.detail.DetailViewModel

class DetailViewModelFactory(private val favoriteUserDao: FavoriteUserDao) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(favoriteUserDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}