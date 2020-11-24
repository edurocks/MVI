package com.joaquimley.heetch.heetchest.location

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import com.joaquimley.heetch.heetchest.location.model.ClosestDriverModel
import com.joaquimley.heetch.heetchest.model.DriverModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
class LocationManagerImpl
@Inject constructor(
    private val locationProviderClient: FusedLocationProviderClient,
    private val locationRequestType: LocationRequest,
    private val backgroundThread: Scheduler,
    private val mainThread: Scheduler
) : LocationManager {

    private val currentLocationStream: BehaviorSubject<LatLng> = BehaviorSubject.create()
    private val closestDistanceStream: BehaviorSubject<ClosestDriverModel> = BehaviorSubject.create()

    private var currentLocation: LatLng by Delegates.observable(LatLng(Double.NaN, Double.NaN)) { _, _, newValue ->
        currentLocationStream.onNext(newValue)
    }

    private var closestDriverModel: ClosestDriverModel by Delegates.observable(ClosestDriverModel()) { _, _, newValue ->
        closestDistanceStream.onNext(newValue)
    }

    private var isListeningToLocationChanges = false

    init {
        observeLocationChanges()
    }

    override fun observeCurrentPosition(): Observable<LatLng> =
        currentLocationStream.subscribeOn(backgroundThread).observeOn(mainThread)


    override fun getClosestDriverPositionFromData(): Observable<ClosestDriverModel> =
        closestDistanceStream.subscribeOn(backgroundThread).observeOn(mainThread)

    override fun getMidpointToClosestPosition(): Single<LatLng> {
        return Single.just(LatLngBounds.builder().include(currentLocation).include(closestDriverModel.position).build().center)
    }

    override fun updateClosestPositionDistanceData(data: List<DriverModel>) {
        var distance: Double = Double.POSITIVE_INFINITY
        data.minBy { driver ->
            SphericalUtil.computeDistanceBetween(currentLocation, driver.position).also {
                if (distance > it) distance = it // Get min distance from the loop
            }
        }?.also {
            if (distance != Double.POSITIVE_INFINITY) {
                closestDriverModel = ClosestDriverModel(it, distance)
            }
        }
    }

    private fun observeLocationChanges() {
        if (isListeningToLocationChanges) {
            return
        }

        try {
            locationProviderClient.requestLocationUpdates(locationRequestType, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    locationResult?.let {
                        currentLocation = LatLng(it.lastLocation.latitude, it.lastLocation.longitude)
                    }
                }
            }, null /* Looper */)
            isListeningToLocationChanges = true
        } catch (securityException: SecurityException) {
            currentLocationStream.onError(securityException)
        }
    }
}