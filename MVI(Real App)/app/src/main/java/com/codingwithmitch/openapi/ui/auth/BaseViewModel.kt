package com.codingwithmitch.openapi.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations


abstract class BaseViewModel<StateEvent, ViewState> : ViewModel()
{

    val TAG: String = "AppDebug"

    val _stateEvent: MutableLiveData<StateEvent> = MutableLiveData()
    val _viewState: MutableLiveData<ViewState> = MutableLiveData()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dataState: LiveData<DataState<ViewState>> = Transformations
        .switchMap(_stateEvent){stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun setStateEvent(event: StateEvent){
        _stateEvent.value = event
    }

    fun getCurrentViewStateOrNew(): ViewState{
        return viewState.value?.let {
            it
        }?: initNewViewState()
    }

    abstract fun handleStateEvent(stateEvent: StateEvent): LiveData<DataState<ViewState>>

    abstract fun initNewViewState(): ViewState

}