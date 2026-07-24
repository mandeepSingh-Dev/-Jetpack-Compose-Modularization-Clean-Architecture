package com.uae.feature.auth.ui.events

import com.uae.core_common.UIEvent
import com.uae.feature.auth.remote.model.response.LoginResponse

sealed interface OTPScreenEvent : UIEvent{
    data class OTPVerifiedSuccess(val loginData : LoginResponse.LoginData?) : OTPScreenEvent
}