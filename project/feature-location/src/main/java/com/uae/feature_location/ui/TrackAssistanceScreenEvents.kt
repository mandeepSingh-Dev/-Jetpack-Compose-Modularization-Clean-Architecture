package com.uae.feature_location.ui

import com.uae.core_common.UIEvent

sealed interface TrackAssistanceScreenEvents : UIEvent{
    object AssistanceResolved : TrackAssistanceScreenEvents
}