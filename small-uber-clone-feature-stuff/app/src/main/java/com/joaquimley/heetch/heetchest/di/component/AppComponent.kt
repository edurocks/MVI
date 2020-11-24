package com.joaquimley.heetch.heetchest.di.component

import android.content.Context
import com.joaquimley.heetch.heetchest.App
import com.joaquimley.heetch.heetchest.di.module.*
import com.joaquimley.heetch.heetchest.di.qualifier.ApplicationContext
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityModule::class,
    LocationModule::class,
    ViewModelModule::class,
    SchedulerModule::class,
    NetworkModule::class,
    RepositoryModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(@ApplicationContext applicationContext: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}