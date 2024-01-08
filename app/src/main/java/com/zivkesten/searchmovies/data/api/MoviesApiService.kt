package com.zivkesten.searchmovies.data.api

import com.zivkesten.searchmovies.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("https://www.omdbapi.com/")
    suspend fun searchMovies(@Query("s") query: String, @Query("page") page: Int, @Query("apikey") apiKey: String = "b3db097d"): MovieResponse
}