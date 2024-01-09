package com.zivkesten.searchmovies.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.zivkesten.searchmovies.data.api.MoviesApiService
import com.zivkesten.searchmovies.data.local.MovieDao
import com.zivkesten.searchmovies.domain.mapper.toDomain
import com.zivkesten.searchmovies.domain.mapper.toDomainMedia
import com.zivkesten.searchmovies.domain.mapper.toEntity
import com.zivkesten.searchmovies.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val apiService: MoviesApiService,
    private val movieDao: MovieDao
) {
    fun searchMovies(searchTerm: String): Flow<PagingData<Movie>> {

        // If cache is empty or outdated, fetch from network
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = true),
            pagingSourceFactory = { MoviesPagingSource(apiService, searchTerm) }
        ).flow
            .map { pagingData -> pagingData.map { it.toDomain() } }
    }

    suspend fun cacheMovies(movies: List<Movie>) {
        movieDao.replaceAll(movies.toEntity())
    }

    suspend fun restoreCache() = movieDao.getAllMedia().toDomainMedia()
}
