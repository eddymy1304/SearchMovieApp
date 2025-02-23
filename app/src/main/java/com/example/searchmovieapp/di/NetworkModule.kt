package com.example.searchmovieapp.di

import com.example.searchmovieapp.BuildConfig
import com.example.searchmovieapp.core.ApiKeyInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TYPE = "application/json"
    private const val TIME_DEFAULT = 30L

    @Provides
    @Singleton
    @Api(ApiType.MAIN)
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val converter = Json.asConverterFactory(TYPE.toMediaType())
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(ApiKeyInterceptor())
            .connectTimeout(TIME_DEFAULT, TimeUnit.SECONDS)
            .readTimeout(TIME_DEFAULT, TimeUnit.SECONDS)
            .writeTimeout(TIME_DEFAULT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Dispatcher(AppDispatchers.IO)
    fun provideIODispatcher() : CoroutineDispatcher = Dispatchers.IO
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Api(val type: ApiType)

enum class ApiType(val type: String) {
    MAIN("main"),
    SECOND("second")
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: AppDispatchers)

enum class AppDispatchers {
    IO,
    DEFAULT,
    MAIN
}