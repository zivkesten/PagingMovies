package com.zivkesten.searchmovies.domain.repository

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zivkesten.searchmovies.data.api.model.MovieDto
import com.zivkesten.searchmovies.data.api.MoviesApiService
import java.io.IOException

private const val STARTING_PAGE_INDEX  = 1
private const val RESPONSE_FAIL = "False"

class MoviesPagingSource(
    private val service: MoviesApiService,
    private val query: String
) : PagingSource<Int, MovieDto>() {


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDto> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.searchMovies(query, position)
            return when  {
                response.response.equals(RESPONSE_FAIL) -> {
                    LoadResult.Error(Exception(response.error))
                }
                (response.totalResults ?: 0) > 0 -> {
                    val movies = response.search as List<MovieDto>
                    LoadResult.Page(
                        data = movies,
                        prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                        nextKey = if (movies.isEmpty()) null else position + 1
                    )
                }
                else -> {
                    Log.e("Zivi", "MoviesPagingSource -> else UNKNOWN")
                    LoadResult.Error(Exception("Unknown"))
                }
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDto>): Int? {
        return null
    }
}