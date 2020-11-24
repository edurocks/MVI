package com.codingwithmitch.mviexample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.codingwithmitch.mviexample.model.BlogPost
import com.codingwithmitch.mviexample.model.User
import com.codingwithmitch.mviexample.repository.Repository
import com.codingwithmitch.mviexample.ui.main.state.MainStateEvent
import com.codingwithmitch.mviexample.ui.main.state.MainViewState
import com.codingwithmitch.mviexample.util.AbsentLiveData
import com.codingwithmitch.mviexample.util.DataState

class MainViewModel : ViewModel(){

    private val _viewState : MutableLiveData<MainViewState> = MutableLiveData()
    private val _stateEvent : MutableLiveData<MainStateEvent> = MutableLiveData()

    val viewState : LiveData<MainViewState>
        get() = _viewState

    val dataState : LiveData<DataState<MainViewState>> = Transformations.switchMap(_stateEvent){ stateEvent ->
            stateEvent?.let {
                handleStateEvent(it)
            }
        }

    private fun handleStateEvent(stateEvent : MainStateEvent): LiveData<DataState<MainViewState>> {
        return when(stateEvent){
            is MainStateEvent.GetBlogPostsEvent -> {
                return Repository.getBlogPosts()
            }

            is MainStateEvent.GetUserEvent -> {
                return Repository.getUser(stateEvent.userId)
            }

            is MainStateEvent.None -> {
                AbsentLiveData.create()
            }
        }
    }

    private fun getCurrentViewStateOrNew() : MainViewState{
        return viewState.value?.let {
            it
        }?: MainViewState()
    }

    fun setBlogListData(blogPosts: List<BlogPost>){
        val update = getCurrentViewStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }

    fun setUser(user: User){
        val update = getCurrentViewStateOrNew()
        update.user = user
        _viewState.value = update
    }

    fun setEventState(event: MainStateEvent){
        _stateEvent.value = event
    }
}