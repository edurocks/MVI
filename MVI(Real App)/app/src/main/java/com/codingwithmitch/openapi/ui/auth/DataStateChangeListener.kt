package com.codingwithmitch.openapi.ui.auth

interface DataStateChangeListener {
    fun onDataStateChange(dataState: DataState<*>?)
}