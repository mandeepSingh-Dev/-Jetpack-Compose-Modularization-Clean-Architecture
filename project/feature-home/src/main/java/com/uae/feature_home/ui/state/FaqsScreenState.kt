package com.uae.feature_home.ui.state

import com.uae.core_common.UIState
import com.uae.feature_home.remote.model.response.FaqsListResponse.FaqsData

data class FaqsScreenState(
    val isLoading : Boolean = false,
    val faqsList: List<FaqsData?>? = emptyList()
) : UIState