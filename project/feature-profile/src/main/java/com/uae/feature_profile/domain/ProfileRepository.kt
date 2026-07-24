package com.uae.feature_profile.domain

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_profile.remote.model.requestBody.ProfileSetupRequestBody
import com.uae.feature_profile.remote.model.response.ProfileResponse
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {


    fun updateProfile(profileSetupRequestBody: ProfileSetupRequestBody?): Flow<NetworkResult<ProfileResponse>>
    fun getProfile(): Flow<NetworkResult<ProfileResponse>>
}