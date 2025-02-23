package com.example.searchmovieapp.di

import android.content.Context
import androidx.room.Room
import com.example.searchmovieapp.data.database.AppDatabase
import com.example.searchmovieapp.data.database.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room
            .databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "movie_db"
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(db: AppDatabase): MovieDao {
        return db.movieDao()
    }

}