package com.zivkesten.searchmovies.viewmodels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.zivkesten.searchmovies.UiState
import com.zivkesten.searchmovies.data.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    var _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    val _searchQuery = MutableStateFlow("")

    // Exposed as a Flow to be collected in the Activity
    @OptIn(FlowPreview::class)
    val searchQueryFlow: Flow<String> = _searchQuery
        .debounce(600) // Debounce for 600 ms
        .filter { it.length >= 3 }
    // Only emit if length is 3 or more

    var loadJob: Job? = null

    init {
        viewModelScope.launch {
            searchQueryFlow.collect {
                getMoviesFlow(it)
            }
        }
    }



    private fun getMoviesFlow(query: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.searchMovies(query)
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    Log.e("Zivi", "pagingData ${pagingData}")
                    try {
                        _uiState.value = UiState.Content(pagingData)
                    } catch (e: Throwable) {
                        Log.e("Zivi", "error ${e.message}")
                    }
                }
        }
    }

    fun onLoadState(state: CombinedLoadStates) {
        // TODO: Add mapper from throwable to human readable message
        Log.e("Zivi", "state.source.refresh: ${state.source.refresh}")
        when (state.source.refresh) {
            is LoadState.Error -> {
                val errorState = state.source.append as? LoadState.Error
                    ?: state.source.prepend as? LoadState.Error
                    ?: state.append as? LoadState.Error
                    ?: state.prepend as? LoadState.Error
                    ?: state.source.refresh
                errorState.let {
                    val error = (it as? LoadState.Error)?.error
                    Log.e("Zivi", "error loading state: ${error?.message}")
                    if (error?.message == "Too many results.") {
                        _uiState.value = UiState.Content("Too many results")
                    } else {
                        _uiState.value = UiState.Error(Exception())
                    }

                }
            }
            is LoadState.Loading -> _uiState.value = UiState.Loading
            else -> Unit
        }

    }



}

