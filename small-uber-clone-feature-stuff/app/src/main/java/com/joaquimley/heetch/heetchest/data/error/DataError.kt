package com.joaquimley.heetch.heetchest.data.error

sealed class DataError(reason: String) : Throwable(reason) {
    class ClosestDriverError : DataError("Failed to get closest driver info")
    class DriversPositionError : DataError("Failed to connect to Drivers")
    class MidpointPositionError : DataError("Failed to get midpoint position")
}
