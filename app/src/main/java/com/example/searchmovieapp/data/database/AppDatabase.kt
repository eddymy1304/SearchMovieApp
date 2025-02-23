package com.example.searchmovieapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.searchmovieapp.data.database.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}