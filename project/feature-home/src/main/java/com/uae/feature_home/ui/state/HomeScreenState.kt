package com.uae.feature_home.ui.state

import com.uae.core_common.UIState
import com.uae.core_network.data.model.TrackAssistanceData
import com.uae.feature_profile.remote.model.response.ProfileResponse

data class HomeScreenState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val userData : ProfileResponse.UserData? = null,
    val trackAssistanceData : TrackAssistanceData? = null,
) : UIState