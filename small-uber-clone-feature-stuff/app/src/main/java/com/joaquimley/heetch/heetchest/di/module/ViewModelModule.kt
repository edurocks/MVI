package com.joaquimley.heetch.heetchest.di.module

import androidx.lifecycle.ViewModel
import com.joaquimley.heetch.heetchest.ui.HomeViewModel
import com.joaquimley.heetch.heetchest.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}
