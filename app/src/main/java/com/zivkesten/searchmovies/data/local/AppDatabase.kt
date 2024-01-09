package com.zivkesten.searchmovies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zivkesten.searchmovies.data.local.model.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
