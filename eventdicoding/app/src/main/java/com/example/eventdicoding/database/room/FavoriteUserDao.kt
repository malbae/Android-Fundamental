package com.example.eventdicoding.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.eventdicoding.database.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Insert
    fun addToFavorite(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user")
    fun getFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count (*) FROM favorite_user WHERE favorite_user.id= :id")
    fun checkUserFavorite(id: Int): Int

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    fun removeFromFavorite(id: Int): Int


}