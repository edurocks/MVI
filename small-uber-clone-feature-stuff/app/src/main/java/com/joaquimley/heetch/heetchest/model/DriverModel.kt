package com.joaquimley.heetch.heetchest.model

import com.google.android.gms.maps.model.LatLng

data class DriverModel(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String,
    var position: LatLng) {

    val displayName: String
        get() = "$firstName $lastName"
}

