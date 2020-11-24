package com.codingwithmitch.openapi.ui.auth.auth

import androidx.lifecycle.LiveData
import com.codingwithmitch.openapi.ui.auth.BaseViewModel
import com.codingwithmitch.openapi.ui.auth.DataState
import com.codingwithmitch.openapi.ui.auth.auth.state.AuthStateEvent
import com.codingwithmitch.openapi.ui.auth.auth.state.AuthViewState
import com.codingwithmitch.openapi.ui.auth.auth.state.LoginFields
import com.codingwithmitch.openapi.ui.auth.auth.state.RegistrationFields
import com.codingwithmitch.openapi.ui.auth.model.AuthToken
import com.codingwithmitch.openapi.ui.auth.repository.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel @Inject constructor(val authRepository: AuthRepository) : BaseViewModel<AuthStateEvent, AuthViewState>(){

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {

        when(stateEvent){
            is AuthStateEvent.LoginAttemptEvent -> {
                return authRepository.attemptLogin(stateEvent.email, stateEvent.password)
            }

            is AuthStateEvent.RegisterAttemptEvent -> {return authRepository.attemptRegistration(stateEvent.email, stateEvent.username,
                                                                                                stateEvent.password, stateEvent.password_confirm)}

            is AuthStateEvent.CheckPreviousAuthEvent -> {
                return authRepository.checkPreviousAuthUser()
            }
        }
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    fun setRegistrationFields(registrationFields: RegistrationFields){
        val update = getCurrentViewStateOrNew()
        if (update.registrationFields == registrationFields){return}
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if (update.loginFields == loginFields){return}
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if (update.authToken == authToken){return}
        update.authToken = authToken
        _viewState.value = update
    }

    fun cancelActiveJobs(){
        authRepository.cancelActiveJobs()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
}