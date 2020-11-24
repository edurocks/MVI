package com.joaquimley.heetch.heetchest.utils

import android.Manifest
import android.app.Activity
import androidx.core.content.PermissionChecker

fun Activity.hasLocationPermission(): Boolean {
    return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
}