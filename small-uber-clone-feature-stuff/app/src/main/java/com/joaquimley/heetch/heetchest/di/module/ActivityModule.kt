package com.joaquimley.heetch.heetchest.di.module

import androidx.lifecycle.ViewModelProvider
import com.joaquimley.heetch.heetchest.di.scopes.ActivityScope
import com.joaquimley.heetch.heetchest.di.viewmodel.ViewModelFactory
import com.joaquimley.heetch.heetchest.ui.HomeActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun contributeHomeActivity(): HomeActivity

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
