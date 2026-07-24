package com.uae.core_location.utils

import android.Manifest
import java.util.concurrent.TimeUnit

object LocationConstants{
    val LOCATION_PERMISSIONS = listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)

    var LOCATION_CALLBACK_REQUEST_INTERVAL = TimeUnit.MINUTES.toMillis(15/* 5000*60 */)
//    var LOCATION_CALLBACK_REQUEST_INTERVAL = 5000L  // TODO UNDO ABOVE COMMENT OF LOCATION INTERVAL.

    const val LOCATION_PROVIDER_REQUEST_CODE = 52
    const val LOCATION = "location"

    const val LOCATION_DISTANCE_KM = 5
}