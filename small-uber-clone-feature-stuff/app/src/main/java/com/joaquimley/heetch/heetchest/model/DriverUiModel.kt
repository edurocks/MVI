package com.joaquimley.heetch.heetchest.model

import java.text.DecimalFormat

data class DriverUiModel(val name: String, val imageUrl: String, var distance: Double = Double.NaN) {

    private val distanceFormatter = DecimalFormat("####")

    val distanceText: String
        get() {
            return "${distanceFormatter.format(distance)} m"
        }
}

