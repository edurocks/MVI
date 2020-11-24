package com.example.nybooks.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewBodyResponse(
    @Json(name = "results")
    val reviewResults: List<ReviewDetailsResponse>
)