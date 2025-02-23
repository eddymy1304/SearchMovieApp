package com.example.searchmovieapp.di

import com.example.searchmovieapp.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApiService(@Api(ApiType.MAIN) retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

}