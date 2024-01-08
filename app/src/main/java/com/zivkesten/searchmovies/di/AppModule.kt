package com.zivkesten.searchmovies.di

import com.zivkesten.searchmovies.data.api.MoviesApiService
import com.zivkesten.searchmovies.data.repository.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMoviesRepository(apiService: MoviesApiService): MoviesRepository {
        return MoviesRepository(apiService)
    }
}
