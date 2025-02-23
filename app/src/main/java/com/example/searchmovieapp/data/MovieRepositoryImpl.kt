package com.example.searchmovieapp.data

import androidx.annotation.WorkerThread
import com.example.searchmovieapp.core.NetworkError
import com.example.searchmovieapp.core.NotDataFound
import com.example.searchmovieapp.data.database.MovieDao
import com.example.searchmovieapp.data.database.asDomainFromDb
import com.example.searchmovieapp.data.database.asDomainFromRemote
import com.example.searchmovieapp.data.database.asEntity
import com.example.searchmovieapp.data.remote.ApiService
import com.example.searchmovieapp.di.AppDispatchers
import com.example.searchmovieapp.di.Dispatcher
import com.example.searchmovieapp.ui.model.MovieModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val movieDao: MovieDao,
    @Dispatcher(AppDispatchers.IO)
    private val ioDispatcher: CoroutineDispatcher
) : MovieRepository {

    @WorkerThread
    override suspend fun searchMovie(query: String, page: Int): Result<List<MovieModel>> {
        return try {
            withContext(ioDispatcher) {
                val response = api.searchMovie(query = query, page = page)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val movies = body.search?.asDomainFromRemote() ?: listOf()
                        Result.success(movies)
                    } else {
                        Result.failure(NotDataFound())
                    }
                } else {
                    Result.failure(NetworkError(response.code().toString()))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFavoriteMovies(): Flow<List<MovieModel>> {
        return movieDao
            .getFavoriteMovies()
            .map { it.asDomainFromDb() }
            .flowOn(ioDispatcher)
    }

    @WorkerThread
    override suspend fun addFavoriteMovie(movie: MovieModel): Result<Unit> {
        return try {
            withContext(ioDispatcher) {
                movieDao.insertMovies(
                    movie
                        .copy(isFavorite = true)
                        .asEntity()
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFavoriteMovie(movie: MovieModel): Result<Unit> {
        return try {
            withContext(ioDispatcher) {
                movieDao.deleteMovies(movie.asEntity())
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}