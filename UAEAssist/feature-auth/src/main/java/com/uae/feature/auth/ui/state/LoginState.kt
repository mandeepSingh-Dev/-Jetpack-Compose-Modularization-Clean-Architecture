package com.uae.feature.auth.ui.state

import com.uae.core_common.UIState
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Stable
data class LoginState(
    val isLoading : Boolean = false,
    val isTermsConditionsChecked : Boolean = false,
    val loginRequestBody: LoginRequestBody? = LoginRequestBody()
) : UIState