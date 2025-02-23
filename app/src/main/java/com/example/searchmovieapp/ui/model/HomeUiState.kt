package com.example.searchmovieapp.ui.model


sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val movies: List<MovieModel> = listOf()) : HomeUiState
    data class Error(val error: String? = null) : HomeUiState
}