package com.joaquimley.heetch.heetchest.di.module

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.joaquimley.heetch.heetchest.di.qualifier.ApplicationContext
import com.joaquimley.heetch.heetchest.di.qualifier.BackgroundThread
import com.joaquimley.heetch.heetchest.di.qualifier.UiThread
import com.joaquimley.heetch.heetchest.location.LocationManager
import com.joaquimley.heetch.heetchest.location.LocationManagerImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
class LocationModule {

    @Provides
    fun provideLocationManger(
        locationProvider: FusedLocationProviderClient,
        locationRequest: LocationRequest,
        @BackgroundThread backgroundThread: Scheduler,
        @UiThread postExecutionThread: Scheduler
    ): LocationManager {
        return LocationManagerImpl(locationProvider, locationRequest, backgroundThread, postExecutionThread)
    }

    @Provides
    fun provideLocationFusionClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun provideLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}
