package com.uae.core_network.data.repositoryImpl

import com.uae.core_common.UserManager
import com.uae.core_network.data.model.AssistanceListResponse
import com.uae.core_network.data.model.LastAcceptedAssistanceResponse
import com.uae.core_network.data.model.requestBody.AssistanceClickRequestBody
import com.uae.core_network.data.service.AssistanceService
import com.uae.core_network.data.service.CommonService
import com.uae.core_network.domain.AssistanceRepository
import com.uae.core_network.domain.CommonRepository
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AssistanceRepositoryImpl  @Inject constructor(
    private val assistanceService: AssistanceService,
    private val networkHelper: NetworkHelper,
) : AssistanceRepository {



    override fun getAssistanceListing(
        page: Int, limit: Int, search: String?, status: Int?,
    ): Flow<NetworkResult<AssistanceListResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<AssistanceListResponse>(
                call = {
                    assistanceService.assistanceListing(
                        page = page,
                        limit = limit,
                        search = search,
                        status = status
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun assistanceClick(assistanceClickRequestBody: AssistanceClickRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithRetryFlow<ApiResponse2>(
                call = {
                    assistanceService.assistanceClick(
                        assistanceClickRequestBody = assistanceClickRequestBody
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getLastAcceptedAssistance(): Flow<NetworkResult<LastAcceptedAssistanceResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<LastAcceptedAssistanceResponse>(
                call = {
                    assistanceService.getLastAcceptedAssistance()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }




}