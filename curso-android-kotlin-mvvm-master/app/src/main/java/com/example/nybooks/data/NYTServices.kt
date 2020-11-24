package com.example.nybooks.data

import com.example.nybooks.data.response.BookBodyResponse
import com.example.nybooks.data.response.LoginResponse
import com.example.nybooks.data.response.ReviewBodyResponse
import com.example.nybooks.data.response.TokenResponse
import retrofit2.Call
import retrofit2.http.*

interface NYTServices {

    @GET("lists.json")
    fun getBooks(
        @Query("api-key") apiKey: String = "iBsAB2KI1gUDoR8VGcTwak69jRlagQm8",
        @Query("list") list: String = "hardcover-fiction"
    ): Call<BookBodyResponse>

    @GET("reviews.json")
    fun getReviewsByAuthor(
        @Query("author") authorName: String,
        @Query("api-key") apiKey: String = "iBsAB2KI1gUDoR8VGcTwak69jRlagQm8"
    ): Call<ReviewBodyResponse>

    @FormUrlEncoded
    @POST("/fleet/token")
    fun loginToken(@Field("username") username: String?, @Field("password") password: String?): Call<TokenResponse?>?

    @GET("fleet/devices/{device_id}")
    fun loginUser(@Path("device_id") deviceId: String?, @Header("Authorization") token: String?): Call<LoginResponse?>?
}