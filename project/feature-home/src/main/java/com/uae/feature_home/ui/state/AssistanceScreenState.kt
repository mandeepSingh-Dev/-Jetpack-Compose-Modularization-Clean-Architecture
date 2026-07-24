package com.uae.feature_home.ui.state

import com.uae.core_common.UIState

data class AssistanceScreenState(
    val isLoading : Boolean = false,
    val isPendingListRefreshing : Boolean = false,
    val isAcceptedRefreshing : Boolean = false,
    val isCompletedListRefreshing : Boolean = false,
) : UIState