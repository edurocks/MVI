package com.joaquimley.heetch.heetchest.data

import com.joaquimley.heetch.heetchest.model.DriverModel
import io.reactivex.Flowable

interface DriversRepository {

    fun getDriversPosition(): Flowable<List<DriverModel>>
}