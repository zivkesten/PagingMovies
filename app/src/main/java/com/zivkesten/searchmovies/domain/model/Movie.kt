package com.zivkesten.searchmovies.domain.model

data class Movie(
    val title: String? = null,
    val year: String? = null,
    val type: String? = null,
    val imdbID: String? = null,
    val poster: String? = null,
)