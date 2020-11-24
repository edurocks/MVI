package com.example.nybooks.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiBooksService {

    private fun initRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor).build()

        return Retrofit.Builder()
        .baseUrl("https://api.nytimes.com/svc/books/v3/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    }

    val service: NYTServices = ApiBooksService.initRetrofit().create(NYTServices::class.java)
  }