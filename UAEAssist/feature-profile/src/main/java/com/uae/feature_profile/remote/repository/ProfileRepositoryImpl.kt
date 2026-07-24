package com.uae.feature_profile.remote.repository

import com.uae.core_common.UserManager
import com.uae.core_common.utils.toJson
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import com.uae.feature_profile.domain.ProfileRepository
import com.uae.feature_profile.remote.model.requestBody.ProfileSetupRequestBody
import com.uae.feature_profile.remote.model.response.ProfileResponse
import com.uae.feature_profile.remote.service.ProfileService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ProfileRepositoryImpl @Inject constructor(
    private val authService: ProfileService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : ProfileRepository {

    override fun updateProfile(profileSetupRequestBody: ProfileSetupRequestBody?): Flow<NetworkResult<ProfileResponse>> {
        return try {
            networkHelper.executeWithFlow<ProfileResponse>(
                call = {
                    authService.updateProfile(body = profileSetupRequestBody)
                },
                onSuccess = { response ->
                    try {

                        val userDataJson = response.body()?.data.toJson()
                        userManager.saveUserData(userData = userDataJson)

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
    override fun getProfile( ): Flow<NetworkResult<ProfileResponse>> {
        return try {
            networkHelper.executeWithFlow<ProfileResponse>(
                call = {
                    authService.getProfile()
                },
                onSuccess = { response ->
                    try {

                        val userDataJson = response.body()?.data.toJson()
                        userManager.saveUserData(userData = userDataJson)

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