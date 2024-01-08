package com.zivkesten.searchmovies.presentation

sealed class UiState {
    data object Loading : UiState()
    class Content<T>(val data: T) : UiState()
    class Error(val exception: Throwable) : UiState()
}
