package com.joaquimley.heetch.heetchest.location

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.nhaarman.mockitokotlin2.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test


class LocationManagerImplTest {

    private val testBackgroundThread = Schedulers.trampoline()
    private val testMainThread = Schedulers.trampoline()

    private val mockLocationProviderClient = mock<FusedLocationProviderClient>()
    private val mockLocationRequest = mock<LocationRequest>()


    private lateinit var locationManager: LocationManagerImpl


    @Before
    fun setUp() {
        locationManager = LocationManagerImpl(mockLocationProviderClient, mockLocationRequest, testBackgroundThread, testMainThread)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `When manager is started, location is observed`() {
        verify(mockLocationProviderClient, times(1))
            .requestLocationUpdates(mockLocationRequest,
                null /* Callback instance created inside need refactor */, null)

    }

    @Test
    fun `When new data is provided, new closest driver position is calculated`() {

    }

    @Test
    fun getMidpointToClosestPosition() {
    }

    @Test
    fun updateClosestPositionDistanceData() {
    }
}