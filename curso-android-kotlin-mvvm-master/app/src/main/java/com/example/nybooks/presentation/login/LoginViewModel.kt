package com.example.nybooks.presentation.login


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nybooks.data.database.UserEntity
import com.example.nybooks.data.model.LoginResult
import com.example.nybooks.data.repository.LoginRepository
import com.example.nybooks.data.response.LoginResponse

@Suppress("UNCHECKED_CAST")
class LoginViewModel(private val dataSource: LoginRepository) : ViewModel() {

    lateinit var userData: LiveData<UserEntity>
    val token = MutableLiveData<String>()
    val loginResponse = MutableLiveData<LoginResponse>()

    fun getToken(username : String, password : String){
        dataSource.loginToken(username, password){ tokenResult ->
            when(tokenResult){
                is LoginResult.Success -> {
                    token.value = tokenResult.token.accessToken
                }
            }
        }
    }

    fun loginUser(deviceId: String, token: String){
        dataSource.loginUser(deviceId, token){ loginResult ->
            when(loginResult){
                is LoginResult.SuccessLogin -> {
                    loginResponse.value = loginResult.loginResponse
                }
            }
        }
    }

    fun getUserData(username: String) {
        dataSource.getUserFromDb(username){ loginResult ->
            when(loginResult){
                is LoginResult.SuccessUserFromDb -> {
                     userData = loginResult.userData
                }
            }
        }
    }

    class ViewModelFactory(private val dataSource: LoginRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(dataSource) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}