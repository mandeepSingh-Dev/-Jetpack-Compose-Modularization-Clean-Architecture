package com.uae.feature_location.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.uae.core.navigation.LocationScreens
import com.uae.core_common.utils.fromJson
import com.uae.core_network.data.model.TrackAssistanceData
import com.uae.feature_location.ui.screens.MyLocationScreen
import com.uae.feature_location.ui.screens.TrackAssistanceScreen


fun EntryProviderScope<NavKey>.locationGraph(backstack: NavBackStack<NavKey>) {

    entry<LocationScreens.TrackAssistanceScreen> {key ->
        val trackAssistanceData = key.trackAssistanceDataJson?.fromJson<TrackAssistanceData>()
        TrackAssistanceScreen(trackAssistanceData = trackAssistanceData)
    }
    entry<LocationScreens.MyLocationScreen> {key ->
        MyLocationScreen()
    }
}


