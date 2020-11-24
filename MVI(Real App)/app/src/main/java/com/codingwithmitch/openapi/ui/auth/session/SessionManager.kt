package com.codingwithmitch.openapi.ui.auth.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codingwithmitch.openapi.ui.auth.model.AuthToken
import com.codingwithmitch.openapi.ui.auth.persistence.AuthTokenDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val authTokenDao: AuthTokenDao, val application: Application){

    private val  _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
    get() = _cachedToken

    fun login(newValue: AuthToken){
        setValue(newValue)
    }

    private fun setValue(newValue: AuthToken?) {
        GlobalScope.launch(Main) {
            if (_cachedToken.value != newValue){
                _cachedToken.value = newValue
            }
        }
    }

    fun logout(){
        CoroutineScope(IO).launch {
            try {
                _cachedToken.value?.account_pk?.let {
                    authTokenDao.nullifyToken(it)
                }?:throw CancellationException("Token Error")
            }catch (e: CancellationException){
                e.stackTrace
            }catch (e: Exception){
                e.stackTrace
            }finally {
                setValue(null)
            }
        }
    }

    fun isConnectedToTheInternet(): Boolean{
        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        try{
            return cm.activeNetworkInfo.isConnected
        }catch (e: Exception){
           e.stackTrace
        }
        return false
    }
}