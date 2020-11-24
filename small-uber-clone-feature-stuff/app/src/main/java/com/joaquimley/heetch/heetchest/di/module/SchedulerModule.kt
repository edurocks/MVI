package com.joaquimley.heetch.heetchest.di.module

import com.joaquimley.heetch.heetchest.di.qualifier.BackgroundThread
import com.joaquimley.heetch.heetchest.di.qualifier.DelayThread
import com.joaquimley.heetch.heetchest.di.qualifier.UiThread
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


@Module
class SchedulerModule {

    @Provides
    @BackgroundThread
    fun providesNetworkScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @DelayThread
    fun providesDelayScheduler(): Scheduler {
        return Schedulers.computation()
    }

    @Provides
    @UiThread
    fun providesUiScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
