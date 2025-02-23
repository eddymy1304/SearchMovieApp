package com.example.searchmovieapp.ui.model

data class MovieModel(
    val id: String,
    val page: Int? = null,
    val title: String,
    val year: Int,
    val poster: String,
    val type: TypeMovie,
    val isFavorite: Boolean = false
)