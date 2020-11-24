package com.codingwithmitch.mviexample.api

import com.codingwithmitch.mviexample.util.LiveDataCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Create a Retrofit Singleton

object RetrofitSingleton{

    private const val BASE_URL = "https://open-api.xyz/"

    private val retrofitSingleton : Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            // convert response to live data. 3 final classes from utils package copy and paste to all projects. These are generic classes.
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
    }

    val apiService : ApiService by lazy {
        retrofitSingleton.build().create(ApiService::class.java)
    }
}