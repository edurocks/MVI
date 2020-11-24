package com.codingwithmitch.openapi.ui.auth.di.auth

import androidx.lifecycle.ViewModel
import com.codingwithmitch.openapi.ui.auth.auth.AuthViewModel
import com.codingwithmitch.openapi.ui.auth.di.viewmodels.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

}