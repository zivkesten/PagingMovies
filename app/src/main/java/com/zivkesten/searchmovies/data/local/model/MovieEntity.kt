package com.zivkesten.searchmovies.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String? = null,
    val year: String? = null,
    val type: String? = null,
    val imdbID: String? = null,
    val poster: String? = null,
)
