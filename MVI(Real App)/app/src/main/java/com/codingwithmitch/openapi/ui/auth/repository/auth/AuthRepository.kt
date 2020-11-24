package com.codingwithmitch.openapi.ui.auth.repository.auth

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.codingwithmitch.openapi.ui.auth.DataState
import com.codingwithmitch.openapi.ui.auth.Response
import com.codingwithmitch.openapi.ui.auth.ResponseType
import com.codingwithmitch.openapi.ui.auth.api.auth.OpenApiAuthService
import com.codingwithmitch.openapi.ui.auth.auth.state.AuthViewState
import com.codingwithmitch.openapi.ui.auth.auth.state.LoginFields
import com.codingwithmitch.openapi.ui.auth.auth.state.RegistrationFields
import com.codingwithmitch.openapi.ui.auth.model.AccountProperties
import com.codingwithmitch.openapi.ui.auth.model.AuthToken
import com.codingwithmitch.openapi.ui.auth.model.LoginResponse
import com.codingwithmitch.openapi.ui.auth.model.RegistrationResponse
import com.codingwithmitch.openapi.ui.auth.persistence.AccountPropertiesDao
import com.codingwithmitch.openapi.ui.auth.persistence.AuthTokenDao
import com.codingwithmitch.openapi.ui.auth.repository.NetworkBoundResource
import com.codingwithmitch.openapi.ui.auth.session.SessionManager
import com.codingwithmitch.openapi.ui.auth.util.*
import com.codingwithmitch.openapi.ui.auth.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.codingwithmitch.openapi.ui.auth.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import kotlinx.coroutines.Job

class AuthRepository constructor(val authTokenDao: AuthTokenDao,
                                 val accountPropertiesDao: AccountPropertiesDao,
                                 val openApiAuthService: OpenApiAuthService,
                                 val sessionManager: SessionManager,
                                 var sharedPreferences: SharedPreferences,
                                 val sharedPreferencesEditor: SharedPreferences.Editor){

    private var repositoryJob: Job? = null

fun attemptLogin(email: String, password: String) : LiveData<DataState<AuthViewState>>{

    val loginFieldsErrors = LoginFields(email, password).isValidForLogin()
    if(loginFieldsErrors != LoginFields.LoginError.none()){
        return returnErrorResponse(loginFieldsErrors, ResponseType.Dialog())
    }

    return object : NetworkBoundResource<LoginResponse, AuthViewState>(
        sessionManager.isConnectedToTheInternet(),
        true
    ){
        override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
            if (response.body.response == GENERIC_AUTH_ERROR){
                return onErrorReturn(response.body.errorMessage, shouldUseDialog = true, shouldUseToast = false
                )
            }

            //Just insert if it does not exist because foreign key relationship
            accountPropertiesDao.insertOrIgnore(AccountProperties(
                    response.body.pk,
                    response.body.email,
                    ""
                )
            )

            //will return -1 if failure
            val result = authTokenDao.insert(AuthToken(
                response.body.pk,
                response.body.token
            ))

            if (result < 0){
                return onCompleteJob(DataState.error(
                    Response(
                        ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog()
                    )
                ))
            }

            saveAuthenticateUserToPrefs(email)

            onCompleteJob(
                DataState.data(
                    AuthViewState( null, null,
                        AuthToken( response.body.pk, response.body.token)
                    )
                )
            )
        }

        override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
            return openApiAuthService.login(email, password)
        }

        override fun setJob(job: Job) {
            repositoryJob?.cancel()
            repositoryJob = job
        }

        override suspend fun createCacheRequestAndReturn() {}

    }.asLiveData()
}

    fun attemptRegistration(email: String, username: String, password: String, confirmPassword: String) : LiveData<DataState<AuthViewState>>{

        val registrationFieldErrors = RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()
        if(registrationFieldErrors != RegistrationFields.RegistrationError.none()){
            return returnErrorResponse(registrationFieldErrors, ResponseType.Dialog())
        }

        return object : NetworkBoundResource<RegistrationResponse, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            true
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {

                if(response.body.response == GENERIC_AUTH_ERROR){
                    return onErrorReturn(response.body.errorMessage, shouldUseDialog = true, shouldUseToast = false
                    )
                }

                //Just insert if it does not exist because foreign key relationship
                accountPropertiesDao.insertOrIgnore(AccountProperties(
                    response.body.pk,
                    response.body.email,
                    ""
                    )
                )

                //will return -1 if failure
                val result = authTokenDao.insert(AuthToken(
                    response.body.pk,
                    response.body.token
                ))

                if (result < 0){
                    return onCompleteJob(DataState.error(
                        Response(
                            ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog()
                        )
                    ))
                }

                onCompleteJob(DataState.data(
                    AuthViewState(null, null,
                        AuthToken(response.body.pk, response.body.token)
                    )
                ))
            }

            override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                return openApiAuthService.register(email, username, password, confirmPassword)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

            override suspend fun createCacheRequestAndReturn() {}

        }.asLiveData()

    }

    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType.Dialog): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }

    fun cancelActiveJobs(){
        repositoryJob?.cancel()
    }

    private fun saveAuthenticateUserToPrefs(email: String) {
        sharedPreferencesEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, email)
        sharedPreferencesEditor.apply()
    }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>>{
        val previousAuthUserEmail: String? = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)

        if (previousAuthUserEmail.isNullOrBlank()){
            return returnNoTokenFound()
        }

        return object : NetworkBoundResource<Void, AuthViewState>(
            sessionManager.isConnectedToTheInternet(),
            false
        ){
            override suspend fun createCacheRequestAndReturn() {
                accountPropertiesDao.searchByEmail(previousAuthUserEmail).let { accountProperties ->
                    accountProperties?.let {
                        if (accountProperties.pk > -1){
                            authTokenDao.searchByPk(accountProperties.pk).let { authToken ->
                                if(authToken != null){
                                    onCompleteJob(DataState.data(
                                        AuthViewState(
                                            authToken = authToken
                                        )
                                    )
                                  )
                                    return
                                }
                            }
                        }
                    }
                }

                onCompleteJob(DataState.data(
                    null,
                    Response(
                        SuccessHandling.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None()
                    )
                ))
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {}

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    private fun returnNoTokenFound(): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.data(null,
                    Response(
                        SuccessHandling.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                        ResponseType.None()
                    )
                )
            }
        }
    }
}