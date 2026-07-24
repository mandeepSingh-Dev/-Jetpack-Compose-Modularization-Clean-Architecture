package com.uae.feature_location.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.uae.core_common.BaseViewModel
import com.uae.core_common.UserManager
import com.uae.core_location.utils.LocationMManager
import com.uae.core_network.domain.AssistanceAllUseCases
import com.uae.feature_location.ui.states.MyLocationScreenState
import com.uae.feature_location.ui.states.TrackAssistanceScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.launch


@HiltViewModel
class MyLocationViewModel@Inject constructor(
    @ApplicationContext  val context : Context,
    private val userManager : UserManager,
    private val assistanceAllUseCases : AssistanceAllUseCases
): BaseViewModel<MyLocationScreenState>(MyLocationScreenState()) {


    val locationManager: LocationMManager by lazy {
        val locationMManager = LocationMManager(
            context = context,
            onLocationResultUpdate = {},
            onLastLocationResultUpdate = {})
        locationMManager.initLocationSDK()
        locationMManager
    }

    init {
        getCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() = viewModelScope.launch {
        locationManager.getCurrentLocation(onLocation = { location ->
            updateState { state ->
                state?.copy(
                    currentLatLng = LatLng(
                        location?.latitude ?: 0.0,
                        location?.longitude ?: 0.0
                    )
                )
            }
        }, onError = {
            Log.d("fkbnkfnbf", it.toString())
        })
    }

}