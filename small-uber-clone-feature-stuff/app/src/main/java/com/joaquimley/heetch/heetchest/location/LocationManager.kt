package com.joaquimley.heetch.heetchest.location

import com.google.android.gms.maps.model.LatLng
import com.joaquimley.heetch.heetchest.location.model.ClosestDriverModel
import com.joaquimley.heetch.heetchest.model.DriverModel
import io.reactivex.Observable
import io.reactivex.Single

interface LocationManager {

    fun getMidpointToClosestPosition(): Single<LatLng>

    fun getClosestDriverPositionFromData(): Observable<ClosestDriverModel>

    fun observeCurrentPosition(): Observable<LatLng>

    fun updateClosestPositionDistanceData(data: List<DriverModel>)
}