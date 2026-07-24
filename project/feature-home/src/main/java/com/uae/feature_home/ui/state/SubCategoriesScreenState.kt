package com.uae.feature_home.ui.state

import com.uae.core_common.UIState

data class SubCategoriesScreenState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
) : UIState