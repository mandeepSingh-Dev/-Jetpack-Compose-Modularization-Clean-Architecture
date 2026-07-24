package com.uae.feature_location.ui.states

import com.google.android.gms.maps.model.LatLng
import com.uae.core_common.UIState
import com.uae.core_network.data.model.TrackAssistanceData

data class MyLocationScreenState(
    val isLoading : Boolean = false,
    val currentLatLng : LatLng?= null,
) : UIState