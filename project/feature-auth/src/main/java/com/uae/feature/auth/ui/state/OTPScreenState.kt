package com.uae.feature.auth.ui.state

import com.uae.core_common.UIState
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody

data class OTPScreenState(
    val loginRequestBody: LoginRequestBody? = null,
    val isLoading : Boolean = false,
    val phoneOTP : String? = null
) : UIState