package com.codingwithmitch.mviexample

import com.codingwithmitch.mviexample.util.DataState

interface DateStateListener {

    fun onDataStateChange(dataState: DataState<*>?)
}