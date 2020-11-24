package com.joaquimley.heetch.heetchest.data.remote.model

data class DriverRemoteModel(val id: Int,
                             val firstname: String,
                             val lastname: String,
                             val image: String,
                             val coordinates: Coordinates) {

    data class Coordinates(val latitude: Double,
                           val longitude: Double)
}