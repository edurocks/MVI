package com.codingwithmitch.openapi.ui.auth.di.auth

import com.codingwithmitch.openapi.ui.auth.auth.ForgotPasswordFragment
import com.codingwithmitch.openapi.ui.auth.auth.LauncherFragment
import com.codingwithmitch.openapi.ui.auth.auth.LoginFragment
import com.codingwithmitch.openapi.ui.auth.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

}