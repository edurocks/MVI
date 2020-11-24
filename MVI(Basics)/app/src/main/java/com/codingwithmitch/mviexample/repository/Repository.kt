package com.codingwithmitch.mviexample.repository

import androidx.lifecycle.LiveData
import com.codingwithmitch.mviexample.api.RetrofitSingleton
import com.codingwithmitch.mviexample.model.BlogPost
import com.codingwithmitch.mviexample.model.User
import com.codingwithmitch.mviexample.ui.main.state.MainViewState
import com.codingwithmitch.mviexample.util.ApiSuccessResponse
import com.codingwithmitch.mviexample.util.DataState
import com.codingwithmitch.mviexample.util.GenericApiResponse

object Repository {

    fun getBlogPosts(): LiveData<iDataState<MainViewState>>{
        return object : NetworkBoundResource<List<BlogPost>, MainViewState>(){
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<List<BlogPost>>) {
                result.value = DataState.data(data = MainViewState(blogPosts = response.body))
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return RetrofitSingleton.apiService.getBlogPosts()
            }

        }.asLiveData()
    }

    fun getUser(userId: String): LiveData<DataState<MainViewState>>{
        return object : NetworkBoundResource<User, MainViewState>(){
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<User>) {
                result.value = DataState.data(data = MainViewState(user = response.body))
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return RetrofitSingleton.apiService.getUser(userId)
            }

        }.asLiveData()
    }

}