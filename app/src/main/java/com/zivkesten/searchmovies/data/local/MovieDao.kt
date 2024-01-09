package com.zivkesten.searchmovies.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.zivkesten.searchmovies.data.local.model.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    suspend fun getAllMedia(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medias: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAll(movies: List<MovieEntity>) {
        deleteAll()
        insertAll(movies)
    }
}
