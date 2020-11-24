package com.codingwithmitch.openapi.ui.auth.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.auth.BaseActivity
import com.codingwithmitch.openapi.ui.auth.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        subscribeObservers()
    }

    private fun subscribeObservers(){
        sessionManager.cachedToken.observe(this, Observer { authToken->
            if(authToken == null || authToken.account_pk == -1 || authToken.token == null){
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
    }

    override fun displayProgressBar(boolean: Boolean) {
        if (boolean){
            progress_bar.visibility = View.VISIBLE
        }else{
            progress_bar.visibility = View.GONE
        }
    }
}