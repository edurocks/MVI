package com.example.nybooks.data.response

import com.squareup.moshi.Json

data class ReviewDetailsResponse(
    @Json(name = "book_title")
    val titleBook: String,
    @Json(name = "book_author")
    val authorBook: String,
    @Json(name = "summary")
    val summary: String
)
