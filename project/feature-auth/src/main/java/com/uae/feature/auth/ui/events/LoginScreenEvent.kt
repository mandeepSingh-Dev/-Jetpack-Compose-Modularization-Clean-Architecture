package com.uae.feature.auth.ui.events

import com.uae.core_common.UIEvent

sealed interface LoginScreenEvent : UIEvent{
    object LoginSuccess : LoginScreenEvent
}