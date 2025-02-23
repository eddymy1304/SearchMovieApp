package com.example.searchmovieapp.di

import com.example.searchmovieapp.data.MovieRepository
import com.example.searchmovieapp.data.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindMovieRepository(repository: MovieRepositoryImpl): MovieRepository

}