package com.joaquimley.heetch.heetchest.location.model

import com.google.android.gms.maps.model.LatLng
import com.joaquimley.heetch.heetchest.model.DriverModel

class ClosestDriverModel(val driver: DriverModel = DriverModel(-1, "", ",", "",
		LatLng(Double.NaN, Double.NaN)), val distance: Double = Double.NaN) {

	val position: LatLng
	    get() = driver.position
}