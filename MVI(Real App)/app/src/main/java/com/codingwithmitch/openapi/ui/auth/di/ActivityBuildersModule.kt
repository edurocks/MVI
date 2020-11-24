package com.codingwithmitch.openapi.ui.auth.di

import com.codingwithmitch.openapi.ui.auth.auth.AuthActivity
import com.codingwithmitch.openapi.ui.auth.di.auth.AuthFragmentBuildersModule
import com.codingwithmitch.openapi.ui.auth.di.auth.AuthModule
import com.codingwithmitch.openapi.ui.auth.di.auth.AuthScope
import com.codingwithmitch.openapi.ui.auth.di.auth.AuthViewModelModule
import com.codingwithmitch.openapi.ui.auth.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )

    abstract fun contributeAuthActivity(): AuthActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}