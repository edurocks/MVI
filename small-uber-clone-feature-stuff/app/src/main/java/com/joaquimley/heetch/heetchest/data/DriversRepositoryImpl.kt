package com.joaquimley.heetch.heetchest.data

import com.joaquimley.heetch.heetchest.data.remote.HiringHeetchApi
import com.joaquimley.heetch.heetchest.model.DriverModel
import io.reactivex.Flowable
import io.reactivex.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DriversRepositoryImpl @Inject constructor(
    private val remote: HiringHeetchApi,
    private val backgroundThread: Scheduler,
    private val mainThread: Scheduler,
    private val delayScheduler: Scheduler,
    private val intervalInSeconds: Long,
    private val mapper: DriverMapper
) : DriversRepository {

    override fun getDriversPosition(): Flowable<List<DriverModel>> {
        return fetchDriversFromRemoteSourceWithRepeat()
    }

    private fun fetchDriversFromRemoteSourceWithRepeat(): Flowable<List<DriverModel>> {
        return remote.getCoordinates()
            .subscribeOn(backgroundThread)
            .observeOn(mainThread)
            .map { mapper.toModel(it) }
            .repeatWhen { it.delay(intervalInSeconds, TimeUnit.SECONDS, delayScheduler) }
    }
}