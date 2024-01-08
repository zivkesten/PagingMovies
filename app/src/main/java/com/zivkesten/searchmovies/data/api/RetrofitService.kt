package com.zivkesten.searchmovies.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitService {

    private val logging: HttpLoggingInterceptor
        get() = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

    private val client: OkHttpClient
        get() = OkHttpClient.Builder().addInterceptor(logging).build()
}
