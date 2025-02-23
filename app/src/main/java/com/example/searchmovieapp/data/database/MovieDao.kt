package com.example.searchmovieapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.searchmovieapp.data.database.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg moviesEntity: MovieEntity)

    @Delete
    suspend fun deleteMovies(vararg moviesEntity: MovieEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Transaction
    @Query("select * from MovieEntity")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Transaction
    @Query("select * from MovieEntity where title like '%' || :query || '%'")
    fun searchMovie(query: String): Flow<List<MovieEntity>>

    @Transaction
    @Query("select * from MovieEntity where isFavorite = 1")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>

}