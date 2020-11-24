package com.example.nybooks.data.repository

import com.example.nybooks.data.model.BooksResult
import com.example.nybooks.data.model.ReviewsResult

interface BooksRepository {
    fun getBooks(booksResultCallback: (result: BooksResult) -> Unit)
    fun getReviewByAuthor(authorName : String, reviewsResultCallback: (result: ReviewsResult) -> Unit)
}