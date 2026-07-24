package com.uae.core_network.domain

import com.uae.core_network.data.model.AssistanceListResponse
import com.uae.core_network.data.model.LastAcceptedAssistanceResponse
import com.uae.core_network.data.model.requestBody.AssistanceClickRequestBody
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AssistanceRepository {
    fun getLastAcceptedAssistance(): Flow<NetworkResult<LastAcceptedAssistanceResponse>>
    fun getAssistanceListing(
        page: Int,
        limit: Int,
        search: String?,
        status: Int?
    ): Flow<NetworkResult<AssistanceListResponse>>


    fun assistanceClick(assistanceClickRequestBody: AssistanceClickRequestBody?): Flow<NetworkResult<ApiResponse2>>
}