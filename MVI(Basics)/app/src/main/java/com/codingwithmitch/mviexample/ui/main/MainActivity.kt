package com.codingwithmitch.mviexample.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.mviexample.DateStateListener
import com.codingwithmitch.mviexample.R
import com.codingwithmitch.mviexample.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DateStateListener
{

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        showMainFragment()
    }

    private fun showMainFragment(){
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                MainFragment(), "MainFragment")
            .commit()
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        handleDataStateChange(dataState)
    }

    private fun handleDataStateChange(dataState: DataState<*>?) {
        dataState?.let {

            //Handle loading
            showProgressBar(it.loading)

            //Handle message
            it.message?.let { message ->
                message.getContentIfNotHandled()?.let { msg ->
                    showToast(msg)
                }
            }
        }
    }

    private fun showToast(message: String){
        Log.e("Toast", message)
    }

    private fun showProgressBar(isVisible: Boolean){
        if (isVisible){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }
}























