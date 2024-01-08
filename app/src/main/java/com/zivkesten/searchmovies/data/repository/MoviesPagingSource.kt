package com.zivkesten.searchmovies.data.repository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zivkesten.searchmovies.MovieDto
import com.zivkesten.searchmovies.MovieResponse
import com.zivkesten.searchmovies.data.api.MoviesApiService
import java.io.IOException

private val STARTING_PAGE_INDEX  = 1
class MoviesPagingSource(
    private val service: MoviesApiService,
    private val query: String
) : PagingSource<Int, MovieDto>() {


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val position = params.key ?: STARTING_PAGE_INDEX
        //Log.d("Zivi", "Service -> position: ${position}")

        return try {
            val response = service.searchMovies(query, position)
            Log.d("Zivi", "Service -> getPhotos: ${response?.search}")
            val state = when  {
                response.response.equals("False") -> {
                    Log.d("Zivi", "response.response.equals(\"False\")  ${response.response.equals("False") }")
                    LoadResult.Error(Exception(response.error))
                }
                (response.totalResults ?: 0) > 0 -> {
                    Log.d("Zivi", "totalResults ${response.totalResults}")

                    val movies = response.search as List<MovieDto>
                    LoadResult.Page(
                        data = movies,
                        prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                        nextKey = if (movies.isEmpty()) null else position + 1

                    )
                }
                else -> {
                    Log.d("Zivi", "Service -> else UNKNOWN")
                    LoadResult.Error(Exception("Unknown"))
                }
            }
            Log.d("Zivi", "Service -> state: ${state}")
            return state

        } catch (exception: IOException) {
            Log.d("Zivi", "Service -> state: ${exception.message}")
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.d("Zivi", "Service -> state: ${exception.message}")
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return null
    }

}