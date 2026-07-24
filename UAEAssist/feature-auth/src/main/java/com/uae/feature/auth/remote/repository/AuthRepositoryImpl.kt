package com.uae.feature.auth.remote.repository

import com.uae.core_common.UserManager
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import com.uae.feature.auth.domain.AuthRepository
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody
import com.uae.feature.auth.remote.model.requestBody.VerifyOtpRequestBody
import com.uae.feature.auth.remote.model.response.LoginResponse
import com.uae.feature.auth.remote.service.AuthService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : AuthRepository {

    override fun login(loginRequestBody: LoginRequestBody?): Flow<NetworkResult<LoginResponse>> {
        return try {
            networkHelper.executeWithFlow<LoginResponse>(
                call = {
                    authService.login(loginRequestBody = loginRequestBody)
                },
                onSuccess = { response ->
                    try {
                        userManager.saveUserToken(response.body()?.data?.token)
                    } catch (e: Exception) {
                    }
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun verifyOTP(verifyOtpRequestBody: VerifyOtpRequestBody?): Flow<NetworkResult<LoginResponse>> {
        return try {
            networkHelper.executeWithFlow<LoginResponse>(
                call = {
                    authService.verifyOTP(body = verifyOtpRequestBody)
                },
                onSuccess = { response ->
                    try {
                        userManager.saveUserToken(response.body()?.data?.token)
                    } catch (e: Exception) {
                    }
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}