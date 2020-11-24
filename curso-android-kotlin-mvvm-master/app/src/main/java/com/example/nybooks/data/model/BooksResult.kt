package com.example.nybooks.data.model

sealed class BooksResult {

    class Success(val books: List<Book>) : BooksResult()
    class ApiError(val responseCode: Int) : BooksResult()
    object ServerError : BooksResult()
}