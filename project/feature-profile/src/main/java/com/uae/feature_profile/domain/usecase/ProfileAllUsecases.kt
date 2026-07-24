package com.uae.feature_profile.domain.usecase

import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_profile.domain.ProfileRepository
import com.uae.feature_profile.remote.model.requestBody.ProfileSetupRequestBody
import com.uae.feature_profile.remote.model.response.ProfileResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class ProfileAllUseCases @Inject constructor(
    val profileUpdateUseCase : ProfileUpdateUseCase,
    val getProfileUseCase : GetProfileUseCase
)



class ProfileUpdateUseCase @Inject constructor(private val profileRepository : ProfileRepository){
    operator fun invoke(profileSetupRequestBody: ProfileSetupRequestBody?): Flow<NetworkResult<ProfileResponse>> {
        return profileRepository.updateProfile(profileSetupRequestBody = profileSetupRequestBody)
    }
}


class GetProfileUseCase @Inject constructor(private val profileRepository : ProfileRepository){
    operator fun invoke(): Flow<NetworkResult<ProfileResponse>> {
        return profileRepository.getProfile()
    }
}
