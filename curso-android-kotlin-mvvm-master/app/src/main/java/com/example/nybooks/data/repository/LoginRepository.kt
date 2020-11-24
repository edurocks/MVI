package com.example.nybooks.data.repository

import com.example.nybooks.data.model.LoginResult

interface LoginRepository {
    fun loginToken(username : String, password : String, loginResultCallback: (result: LoginResult) -> Unit)
    fun loginUser(deviceId : String, token : String, loginResultCallback: (result: LoginResult) -> Unit)
    fun getUserFromDb(username : String, loginResultCallback: (result: LoginResult) -> Unit)
}