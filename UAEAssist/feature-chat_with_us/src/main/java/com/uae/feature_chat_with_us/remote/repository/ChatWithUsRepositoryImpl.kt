package com.uae.feature_chat_with_us.remote.repository

import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import com.uae.feature_chat_with_us.domain.ChatWithUsRepository
import com.uae.feature_chat_with_us.remote.model.requestBody.CreateTickerRequestBody
import com.uae.feature_chat_with_us.remote.model.requestBody.SendMessageBody
import com.uae.feature_chat_with_us.remote.model.response.ChatSupportTicketsListResponse
import com.uae.feature_chat_with_us.remote.model.response.ChatsListResponse
import com.uae.feature_chat_with_us.remote.service.ChatWithUsService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class ChatWithUsRepositoryImpl @Inject constructor(
    private val chatWithUsService: ChatWithUsService,
    private val networkHelper: NetworkHelper,
) : ChatWithUsRepository {


    override fun getChatTickets(
        page: Int,
        limit: Int,
        status: Int
    ): Flow<NetworkResult<ChatSupportTicketsListResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<ChatSupportTicketsListResponse>(
                call = {
                    chatWithUsService.getChatWithUsList(
                        page = page,
                        limit = limit,
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

    override fun getChatsList(
        id : String,
        page: Int,
        limit: Int,
    ): Flow<NetworkResult<ChatsListResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<ChatsListResponse>(
                call = {
                    chatWithUsService.getChatsList(
                        id = id,
                        page = page,
                        limit = limit,
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


    override fun createTicket(
        createTickerRequestBody: CreateTickerRequestBody?
    ): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithRetryFlow<ApiResponse2>(
                call = {
                    chatWithUsService.createTicket(
                        createTickerRequestBody = createTickerRequestBody
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun sendMessage(
        sendMessageBody: SendMessageBody?
    ): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithRetryFlow<ApiResponse2>(
                call = {
                    chatWithUsService.sendMessage(
                        sendMessageBody = sendMessageBody
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

}