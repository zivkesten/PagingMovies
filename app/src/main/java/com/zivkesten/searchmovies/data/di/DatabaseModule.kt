package com.zivkesten.searchmovies.data.di

import android.content.Context
import androidx.room.Room
import com.zivkesten.searchmovies.data.local.AppDatabase
import com.zivkesten.searchmovies.data.local.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideYourDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "movies"
        ).build()
    }

    @Provides
    fun provideYourDao(appDatabase: AppDatabase): MovieDao {
        return appDatabase.movieDao()
    }
}
