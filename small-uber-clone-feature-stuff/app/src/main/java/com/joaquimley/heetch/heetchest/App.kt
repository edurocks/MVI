package com.joaquimley.heetch.heetchest

import android.app.Activity
import android.app.Application
import com.joaquimley.heetch.heetchest.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App: Application(), HasActivityInjector {

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): DispatchingAndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }
}
