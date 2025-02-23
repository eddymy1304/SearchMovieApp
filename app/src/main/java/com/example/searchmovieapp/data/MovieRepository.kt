package com.example.searchmovieapp.data

import androidx.annotation.WorkerThread
import com.example.searchmovieapp.ui.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    @WorkerThread
    suspend fun searchMovie(query: String, page: Int): Result<List<MovieModel>>

    @WorkerThread
    fun getFavoriteMovies(): Flow<List<MovieModel>>

    @WorkerThread
    suspend fun addFavoriteMovie(movie: MovieModel): Result<Unit>

    @WorkerThread
    suspend fun removeFavoriteMovie(movie: MovieModel): Result<Unit>
}