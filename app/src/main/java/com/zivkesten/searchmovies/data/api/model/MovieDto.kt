package com.zivkesten.searchmovies.data.api.model

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("Search")
    val search: List<MovieDto> ? = null,
    @SerializedName("totalResults")
    val totalResults: Int? = null,
    @SerializedName("Response")
    val response: String? = null,
    @SerializedName("Error")
    val error:String? = null
)
data class MovieDto(
    @SerializedName("Title") val title: String? = null,
    @SerializedName("Year") val year: String? = null,
    @SerializedName("Type") val type: String? = null,
    @SerializedName("imdbID") val imdbID: String? = null,
    @SerializedName("Poster") val poster: String? = null,
)