package com.uae.feature_home.ui.events

import com.uae.core_common.UIEvent

sealed interface RateUsScreenEvents : UIEvent{

    object RatingAddedSuccessfully : RateUsScreenEvents
}