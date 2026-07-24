package com.uae.feature.auth.domain

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody
import com.uae.feature.auth.remote.model.requestBody.VerifyOtpRequestBody
import com.uae.feature.auth.remote.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun login(loginRequestBody: LoginRequestBody?) : Flow<NetworkResult<LoginResponse>>

    fun verifyOTP(verifyOtpRequestBody: VerifyOtpRequestBody?): Flow<NetworkResult<LoginResponse>>
}