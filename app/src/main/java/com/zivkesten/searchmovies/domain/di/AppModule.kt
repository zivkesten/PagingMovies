package com.zivkesten.searchmovies.domain.di

import com.zivkesten.searchmovies.data.api.MoviesApiService
import com.zivkesten.searchmovies.data.local.MovieDao
import com.zivkesten.searchmovies.domain.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoviesRepository(
        apiService: MoviesApiService,
        movieDao: MovieDao
    ): MoviesRepository {
        return MoviesRepository(apiService, movieDao)
    }
}
