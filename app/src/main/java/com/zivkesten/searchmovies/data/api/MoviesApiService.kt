package com.zivkesten.searchmovies.data.api

import androidx.paging.PagingData
import com.zivkesten.searchmovies.MovieDto
import com.zivkesten.searchmovies.MovieResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("https://www.omdbapi.com/")
    suspend fun searchMovies(@Query("s") query: String, @Query("page") page: Int, @Query("apikey") apiKey: String = "b3db097d"): MovieResponse
}