package com.uae.core_network.domain

import com.uae.core_network.data.model.BloodGroupsListResponse
import com.uae.core_network.data.model.LastAcceptedAssistanceResponse
import com.uae.core_network.data.model.requestBody.AssistanceClickRequestBody
import com.uae.core_network.domain.CommonUseCases.GetMedicalConditionsUseCase
import com.uae.core_network.domain.CommonUseCases.ImageUploadUseCase
import com.uae.core_network.domain.CommonUseCases.LogoutUseCase
import com.uae.core_network.domain.CommonUseCases.UpdateFcmTokenUseCase
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AssistanceAllUseCases @Inject constructor(
    val getLastAcceptedUseCase : GetLastAcceptedUseCase,
    val assistanceClickUseCase : AssistanceClickUseCase,
)


class GetLastAcceptedUseCase @Inject constructor(private val assistanceRepository : AssistanceRepository){
    operator fun invoke(): Flow<NetworkResult<LastAcceptedAssistanceResponse>> {
        return assistanceRepository.getLastAcceptedAssistance()
    }
}

class AssistanceClickUseCase @Inject constructor(private val assistanceRepository: AssistanceRepository){

    operator fun invoke(assistanceClickRequestBody: AssistanceClickRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return assistanceRepository.assistanceClick(assistanceClickRequestBody = assistanceClickRequestBody)
    }
}