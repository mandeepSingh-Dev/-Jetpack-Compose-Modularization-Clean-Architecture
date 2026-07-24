package com.uae.feature.auth.domain.usecase

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature.auth.domain.AuthRepository
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody
import com.uae.feature.auth.remote.model.requestBody.VerifyOtpRequestBody
import com.uae.feature.auth.remote.model.response.LoginResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class AuthAllUseCases @Inject constructor(
    val loginUseCase : LoginUseCase,
    val verifyOTPUseCase : VerifyOTPUseCase,
)



class LoginUseCase @Inject constructor(private val authRepository : AuthRepository){
    operator fun invoke(loginRequestBody: LoginRequestBody?): Flow<NetworkResult<LoginResponse>> {
        return authRepository.login(loginRequestBody = loginRequestBody)
    }
}
class VerifyOTPUseCase @Inject constructor(private val authRepository : AuthRepository){
    operator fun invoke(verifyOtpRequestBody: VerifyOtpRequestBody?): Flow<NetworkResult<LoginResponse>> {
        return authRepository.verifyOTP(verifyOtpRequestBody = verifyOtpRequestBody)
    }
}