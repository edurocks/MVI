package com.example.nybooks.data.repository

import android.app.Application
import android.util.Log
import androidx.room.withTransaction
import com.example.nybooks.data.ApiBooksService
import com.example.nybooks.data.ApiLoginService
import com.example.nybooks.data.database.AppDatabase
import com.example.nybooks.data.database.UserEntity
import com.example.nybooks.data.model.Book
import com.example.nybooks.data.model.BooksResult
import com.example.nybooks.data.model.ReviewsResult
import com.example.nybooks.data.model.LoginResult
import com.example.nybooks.data.response.BookBodyResponse
import com.example.nybooks.data.response.LoginResponse
import com.example.nybooks.data.response.ReviewBodyResponse
import com.example.nybooks.data.response.TokenResponse
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BooksApiDataSource(application: Application) : BooksRepository, LoginRepository {

    private var appDatabase: AppDatabase = AppDatabase.getInstance(application)

    override fun getBooks(booksResultCallback: (result: BooksResult) -> Unit){
        ApiBooksService.service.getBooks().enqueue(object : Callback<BookBodyResponse> {
            override fun onResponse(call: Call<BookBodyResponse>, response: Response<BookBodyResponse>) {
                when {
                    response.isSuccessful -> {
                        val books: MutableList<Book> = mutableListOf()

                        response.body()?.let { bookBodyResponse ->
                            for (result in bookBodyResponse.bookResults) {
                                val book = result.bookDetailResponses[0].getBookModel()
                                books.add(book)
                            }
                        }

                        booksResultCallback.invoke(BooksResult.Success(books))
                    }

                    else -> booksResultCallback.invoke(BooksResult.ApiError(response.code()))
                }
            }

            override fun onFailure(call: Call<BookBodyResponse>, t: Throwable) {
                booksResultCallback.invoke(BooksResult.ServerError)
            }
        })
    }

    override fun getReviewByAuthor(authorName: String, reviewsResultCallback: (result: ReviewsResult) -> Unit) {
        ApiBooksService.service.getReviewsByAuthor(authorName).enqueue(object : Callback<ReviewBodyResponse>{

            override fun onResponse(call: Call<ReviewBodyResponse>, response: Response<ReviewBodyResponse>) {
                if (response.isSuccessful){
                    val reviewResults = response.body()?.reviewResults
                    reviewResults?.let { reviewResultsFinal ->
                        reviewsResultCallback.invoke(ReviewsResult.Success(reviewResultsFinal))
                    }
                }else{
                    reviewsResultCallback.invoke(ReviewsResult.ApiError(response.code()))
                }
            }

            override fun onFailure(call: Call<ReviewBodyResponse>, t: Throwable) {
                reviewsResultCallback.invoke(ReviewsResult.ServerError)
            }
        })
    }

    override fun loginToken(username: String, password: String, loginResultCallback: (result: LoginResult) -> Unit) {
        ApiLoginService.service.loginToken(username, password)?.enqueue(object : Callback<TokenResponse?>{

            override fun onResponse(call: Call<TokenResponse?>, response: Response<TokenResponse?>) {
                if (response.isSuccessful){
                    response.body()?.let { token ->
                        loginResultCallback.invoke(LoginResult.Success(token))
                    }
                }
            }

            override fun onFailure(call: Call<TokenResponse?>, t: Throwable) {
                Log.e("Error token", t.message)
            }
        })
    }

    override fun loginUser(deviceId: String, token: String, loginResultCallback: (result: LoginResult) -> Unit) {
        ApiLoginService.service.loginUser(deviceId, "Bearer $token")?.enqueue(object : Callback<LoginResponse?>{

            override fun onResponse(call: Call<LoginResponse?>, response: Response<LoginResponse?>) {
                if (response.isSuccessful){
                    response.body()?.let { loginResponse ->
                        loginResultCallback.invoke(LoginResult.SuccessLogin(loginResponse))

                        // insert user data into database
                        val userEntity = UserEntity()
                        userEntity.deviceId = loginResponse.deviceId
                        userEntity.email = loginResponse.email
                        userEntity.name = loginResponse.name
                        userEntity.username = loginResponse.username

                        CoroutineScope(Dispatchers.IO).launch {
                            insertUser(userEntity)
                        }
                    }
                }
            }

            private suspend fun insertUser(userEntity: UserEntity) {
                appDatabase.withTransaction {
                    appDatabase.userDao().insertUser(userEntity)
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
                Log.e("Error login", t.message)
            }
        })
    }

    override fun getUserFromDb(username : String, loginResultCallback: (result: LoginResult) -> Unit) {
        loginResultCallback.invoke(LoginResult.SuccessUserFromDb(appDatabase.userDao().getUserData(username)))
    }
}