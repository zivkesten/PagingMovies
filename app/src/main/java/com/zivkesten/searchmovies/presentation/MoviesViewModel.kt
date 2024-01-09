package com.zivkesten.searchmovies.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zivkesten.searchmovies.domain.model.Movie
import com.zivkesten.searchmovies.domain.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


private const val TOO_MANY_RESULTS = "Too many results."
private const val WELCOME_MESSAGE = "WELCOME!"
private const val NOT_FOUND = "Movie not found!"

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _uiState = MutableStateFlow<UiState>(UiState.Content(WELCOME_MESSAGE))
    val uiState: StateFlow<UiState> = _uiState

    val searchQuery = MutableStateFlow("")

    // Exposed as a Flow to be collected in the Activity
    @OptIn(FlowPreview::class)
    val searchQueryFlow: Flow<String> = searchQuery
        .debounce(600) // Debounce for 600 ms
        .filter { it.length >= 3 } // Only emit if length is 3 or more

    private var loadJob: Job? = null

    init {

        viewModelScope.launch {
            restoreFromCache()
            searchQueryFlow.collect {
                getMoviesFlow(it)
                savedStateHandle[SEARCH_QUERY_KEY] = it
            }
        }
        searchQuery.value = savedStateHandle.get<String>(SEARCH_QUERY_KEY) ?: ""
    }

    private suspend fun MoviesViewModel.restoreFromCache() {
        val cache = repository.restoreCache()
        if (cache.isNotEmpty()) {
            updateUi(PagingData.from(cache))
        } else {
            resetScreen()
        }
    }

    private fun getMoviesFlow(query: String) {
        loadJob?.cancel()

        loadJob = viewModelScope.launch(Dispatchers.IO) {
            repository.searchMovies(query)
                .cachedIn(viewModelScope)
                .collect { updateUi(it) }
        }
    }

    private fun resetScreen() {
        _uiState.value = UiState.Content(WELCOME_MESSAGE)
    }

    private suspend fun updateUi(pagingData: PagingData<Movie>) {
        withContext(Dispatchers.Main) {
            try {
                _uiState.value = UiState.Content(pagingData)
            } catch (e: Throwable) {
                Log.e("Zivi", "error ${e.message}")
                _uiState.value = UiState.Error(e)
            }
        }
    }

    fun onLoadState(state: CombinedLoadStates) {
        when (state.source.refresh) {
            is LoadState.Error -> {
                val errorState = state.source.append as? LoadState.Error
                    ?: state.source.prepend as? LoadState.Error
                    ?: state.append as? LoadState.Error
                    ?: state.prepend as? LoadState.Error
                    ?: state.source.refresh
                errorState.let {
                    val error = (it as? LoadState.Error)?.error
                    when (error?.message) {
                        TOO_MANY_RESULTS -> _uiState.value = UiState.Content(error.message)
                        NOT_FOUND -> _uiState.value = UiState.Content(error.message)
                        else -> _uiState.value = UiState.Error(Exception("Unknown"))
                    }
                }
            }
            else -> Unit // No need to handle other cases ATM
        }
    }

    fun cacheItems(items: List<Movie>?) {
        items?.let {
            viewModelScope.launch(Dispatchers.IO) {
                repository.cacheMovies(items)
            }
        }
    }

    companion object {
        private const val SEARCH_QUERY_KEY = "searchQuery"
    }
}

