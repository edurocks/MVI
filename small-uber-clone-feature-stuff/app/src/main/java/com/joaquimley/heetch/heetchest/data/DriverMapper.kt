package com.joaquimley.heetch.heetchest.data

import com.google.android.gms.maps.model.LatLng
import com.joaquimley.heetch.heetchest.data.remote.model.DriverRemoteModel
import com.joaquimley.heetch.heetchest.di.qualifier.ApiImageUrl
import com.joaquimley.heetch.heetchest.di.qualifier.ApiUrl
import com.joaquimley.heetch.heetchest.model.DriverModel
import com.joaquimley.heetch.heetchest.model.DriverUiModel
import javax.inject.Inject

class DriverMapper @Inject constructor(@ApiImageUrl private val baseUrl: String) {

    fun toUi(from: List<DriverModel>): List<DriverUiModel> {
        return from.map { toUi(it) }
    }

    fun toUi(from: DriverModel, distance: Double = Double.NaN): DriverUiModel {
        return DriverUiModel(from.displayName, "$baseUrl${from.profilePictureUrl}", distance)
    }

    fun toModel(from: List<DriverRemoteModel>): List<DriverModel> {
        return from.map { toModel(it) }
    }

    fun toModel(from: DriverRemoteModel): DriverModel {
        return DriverModel(
            from.id, from.firstname, from.lastname, from.image,
            LatLng(from.coordinates.latitude, from.coordinates.longitude)
        )
    }

}
