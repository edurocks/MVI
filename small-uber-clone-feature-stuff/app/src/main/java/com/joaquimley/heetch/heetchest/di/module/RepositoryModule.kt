package com.joaquimley.heetch.heetchest.di.module

import com.joaquimley.heetch.heetchest.data.DriverMapper
import com.joaquimley.heetch.heetchest.data.DriversRepository
import com.joaquimley.heetch.heetchest.data.DriversRepositoryImpl
import com.joaquimley.heetch.heetchest.data.remote.HiringHeetchApi
import com.joaquimley.heetch.heetchest.di.qualifier.BackgroundThread
import com.joaquimley.heetch.heetchest.di.qualifier.DelayThread
import com.joaquimley.heetch.heetchest.di.qualifier.DelayTimeInSeconds
import com.joaquimley.heetch.heetchest.di.qualifier.UiThread
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler


@Module
class RepositoryModule {

    @Provides
    fun provideDriversRepository(remote: HiringHeetchApi,
                                 @BackgroundThread backgroundThread: Scheduler,
                                 @UiThread postExecutionThread: Scheduler,
                                 @DelayThread delayScheduler: Scheduler,
                                 @DelayTimeInSeconds delay: Long,
                                 mapper: DriverMapper): DriversRepository
            = DriversRepositoryImpl(remote, backgroundThread, postExecutionThread, delayScheduler, delay, mapper)

    @Provides
    @DelayTimeInSeconds
    fun provideDelay(): Long = 5
}
