package com.example.nybooks.data.model

import androidx.lifecycle.LiveData
import com.example.nybooks.data.database.UserEntity
import com.example.nybooks.data.response.LoginResponse
import com.example.nybooks.data.response.TokenResponse

sealed class LoginResult {
   class Success(val token: TokenResponse) : LoginResult()
   class SuccessLogin(val loginResponse: LoginResponse) : LoginResult()
   class SuccessUserFromDb(val userData: LiveData<UserEntity>) : LoginResult()
}