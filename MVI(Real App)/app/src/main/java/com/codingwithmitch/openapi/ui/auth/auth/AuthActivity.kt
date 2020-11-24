package com.codingwithmitch.openapi.ui.auth.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.auth.BaseActivity
import com.codingwithmitch.openapi.ui.auth.ResponseType
import com.codingwithmitch.openapi.ui.auth.auth.state.AuthStateEvent
import com.codingwithmitch.openapi.ui.auth.di.viewmodels.ViewModelProviderFactory
import com.codingwithmitch.openapi.ui.auth.main.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

class AuthActivity : BaseActivity(), NavController.OnDestinationChangedListener{

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        findNavController(R.id.auth_nav_host_fragment).addOnDestinationChangedListener(this)

        subscribeObservers()
        checkPreviousAuthUser()
    }

    private fun subscribeObservers() {

        viewModel.dataState.observe(this, Observer { dataState ->

            onDataStateChange(dataState)

            dataState.data?.let { data ->

                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let { token ->
                            viewModel.setAuthToken(token)
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(this, Observer {
            it.authToken?.let {authToken ->
                sessionManager.login(authToken)
            }
        })

        sessionManager.cachedToken.observe(this, Observer {authToken ->
            if(authToken != null || authToken?.account_pk != -1 || authToken.token != null){
                val intent = Intent(this, MainActivity::class.java)
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

    fun checkPreviousAuthUser(){
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAuthEvent())
    }

    //Detect when fragment is changed
    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        viewModel.cancelActiveJobs()
    }
}