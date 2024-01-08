package com.zivkesten.searchmovies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zivkesten.searchmovies.MovieDto
import com.zivkesten.searchmovies.MovieResponse
import com.zivkesten.searchmovies.data.api.MoviesApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val apiService: MoviesApiService) {

    suspend fun searchMovies(searchTerm: String): Flow<PagingData<MovieDto>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = true
            ),
            pagingSourceFactory = { MoviesPagingSource(apiService, searchTerm) }
        ).flow
    }
}
