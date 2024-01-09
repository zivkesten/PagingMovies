package com.zivkesten.searchmovies.domain.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.zivkesten.searchmovies.data.api.MoviesApiService
import com.zivkesten.searchmovies.data.api.model.MovieDto
import com.zivkesten.searchmovies.data.local.MovieDao
import com.zivkesten.searchmovies.data.local.MovieEntity
import com.zivkesten.searchmovies.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val apiService: MoviesApiService,
    private val movieDao: MovieDao
) {

    suspend fun searchMovies(searchTerm: String): Flow<PagingData<Movie>> {

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

private fun MovieDto.toDomain() = Movie(
    imdbID = this.imdbID,
    title = this.title,
    year = this.year,
    type = this.type,
    poster = this.poster
)

private fun List<Movie>.toEntity() = map {
    MovieEntity(
        id = it.imdbID.hashCode(),
        title = it.title,
        year = it.year,
        type = it.type,
        poster = it.poster
    )
}

private fun MovieDto.toEntity() = MovieEntity(
    id = imdbID.hashCode(),
    title = this.title,
    year = this.year,
    type = this.type,
    poster = this.poster

)

private fun List<MovieEntity>.toDomainMedia() = map {
        Movie(
            title = it.title,
            year = it.year,
            imdbID = it.imdbID,
            poster = it.poster,
            type = it.type
        )
    }
