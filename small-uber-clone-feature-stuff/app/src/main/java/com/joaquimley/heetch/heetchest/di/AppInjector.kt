package com.joaquimley.heetch.heetchest.di

import com.joaquimley.heetch.heetchest.App
import com.joaquimley.heetch.heetchest.di.component.DaggerAppComponent

object AppInjector {
    fun init(app: App) {
        DaggerAppComponent
            .builder()
            .applicationContext(app.applicationContext)
            .build()
            .inject(app)
    }
}
