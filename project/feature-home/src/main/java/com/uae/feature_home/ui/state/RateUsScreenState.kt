package com.uae.feature_home.ui.state

import com.uae.core_common.UIState
import com.uae.feature_home.remote.model.requestBody.RatingRequestBody

data class RateUsScreenState(
    val isLoading : Boolean = false,
    val ratingRequestBody: RatingRequestBody? = RatingRequestBody()
) : UIState