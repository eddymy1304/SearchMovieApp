package com.example.searchmovieapp.data.remote

import com.example.searchmovieapp.data.remote.response.SearchMovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("?")
    suspend fun searchMovie(
        @Query("s") query: String,
        @Query("page") page: Int = 1,
    ): Response<SearchMovieResponse>


}