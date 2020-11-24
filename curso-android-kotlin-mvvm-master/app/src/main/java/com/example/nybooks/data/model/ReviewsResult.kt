package com.example.nybooks.data.model

import com.example.nybooks.data.response.ReviewDetailsResponse

sealed class ReviewsResult {
    class Success(val reviews: List<ReviewDetailsResponse>) : ReviewsResult()
    class ApiError(val responseCode: Int) : ReviewsResult()
    object ServerError : ReviewsResult()
}
