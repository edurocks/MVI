package com.joaquimley.heetch.heetchest.util

import androidx.annotation.RestrictTo
import com.google.android.gms.maps.model.LatLng
import com.joaquimley.heetch.heetchest.data.remote.model.DriverRemoteModel
import com.joaquimley.heetch.heetchest.location.model.ClosestDriverModel
import com.joaquimley.heetch.heetchest.model.DriverModel
import com.joaquimley.heetch.heetchest.model.DriverUiModel
import kotlin.random.Random

@RestrictTo(RestrictTo.Scope.TESTS)
object DataFactory {

    // Models

    fun driverRemoteModel(): DriverRemoteModel {
        return DriverRemoteModel(Random.nextInt(), randomString(), randomString(), randomString(), driverRemoteCoordinates())
    }

    fun driverRemoteCoordinates(): DriverRemoteModel.Coordinates {
        return DriverRemoteModel.Coordinates(Random.nextDouble(), Random.nextDouble())
    }

    fun closestDriverModel(): ClosestDriverModel {
        return ClosestDriverModel(driverModel(), Random.nextDouble())
    }

    fun driverUiModel(): DriverUiModel {
        return DriverUiModel(randomString(), randomString(), Random.nextDouble())
    }

    fun driverModel(): DriverModel {
        return DriverModel(Random.nextInt(), randomString(), randomString(), randomString(), latLng())
    }

    fun driverModelList(count: Int = 5): List<DriverModel> {
        return mutableListOf<DriverModel>().apply {
            repeat(count) {
                add(driverModel())
            }
        }
    }

    fun driverUiModelList(count: Int = 5): List<DriverUiModel> {
        return mutableListOf<DriverUiModel>().apply {
            repeat(count) {
                add(driverUiModel())
            }
        }
    }

    fun driverRemoteModelList(count: Int = 5): List<DriverRemoteModel> {
        return mutableListOf<DriverRemoteModel>().apply {
            repeat(count) {
                add(driverRemoteModel())
            }
        }
    }

    // Generic

    fun latLng(): LatLng {
        return LatLng(Random.nextDouble(), Random.nextDouble())
    }

    fun randomString(): String {
        return "Random string ${Random.nextInt()}"
    }
}