package com.example.searchmovieapp.core

import android.util.Log
import com.example.searchmovieapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    companion object {
        private const val PARAMETER = "apikey"
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        val requestInit = chain.request()
        val urlInit = requestInit.url

        val urlNew = urlInit.newBuilder()
            .addQueryParameter(PARAMETER, BuildConfig.API_KEY)
            .build()

        Log.d("Interceptor", urlNew.toString())

        val requestNew = requestInit.newBuilder()
            .url(urlNew)
            .build()

        return chain.proceed(requestNew)
    }
}