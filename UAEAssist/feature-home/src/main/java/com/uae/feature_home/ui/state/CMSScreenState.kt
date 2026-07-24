package com.uae.feature_home.ui.state

import com.uae.core_common.UIState

data class CMSScreenState(
     val isLoading : Boolean = false,
     val description:  String? = null
) : UIState