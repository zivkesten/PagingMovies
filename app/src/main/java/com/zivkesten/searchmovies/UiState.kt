package com.zivkesten.searchmovies

sealed class UiState {

    object Loading : UiState()

    class Content<T>(val data: T) : UiState()

    class Error(val exception: Throwable) : UiState()
}
