package com.codingwithmitch.openapi.ui.auth.auth

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.codingwithmitch.openapi.ui.auth.di.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

//Base fragment class to avoid repeated code across the several fragments
abstract class BaseAuthFragment : DaggerFragment(){

    val TAG: String = "AppDebug"

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }
}