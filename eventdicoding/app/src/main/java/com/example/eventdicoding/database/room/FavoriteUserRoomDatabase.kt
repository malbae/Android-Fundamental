package com.example.eventdicoding.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventdicoding.database.local.entity.FavoriteUser

@Database(
    entities = [FavoriteUser::class],
    version = 3, // Increment this version number
    exportSchema = false
)
abstract class FavoriteUserRoomDatabase : RoomDatabase() {

    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteUserRoomDatabase? = null

        fun getDatabase(context: Context): FavoriteUserRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteUserRoomDatabase::class.java,
                    "favorite_database"
                )
                    .fallbackToDestructiveMigration() // Automatically handles migration but wipes data.
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
