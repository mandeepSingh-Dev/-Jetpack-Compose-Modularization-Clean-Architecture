package com.uae.feature_chat_with_us.domain

import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_chat_with_us.remote.model.requestBody.CreateTickerRequestBody
import com.uae.feature_chat_with_us.remote.model.requestBody.SendMessageBody
import com.uae.feature_chat_with_us.remote.model.response.ChatSupportTicketsListResponse
import com.uae.feature_chat_with_us.remote.model.response.ChatsListResponse
import kotlinx.coroutines.flow.Flow

interface ChatWithUsRepository {


    fun getChatTickets(
        page: Int,
        limit: Int,
        status: Int
    ): Flow<NetworkResult<ChatSupportTicketsListResponse>>

    fun createTicket(createTickerRequestBody: CreateTickerRequestBody?): Flow<NetworkResult<ApiResponse2>>
    fun getChatsList(
        id: String,
        page: Int,
        limit: Int,
    ): Flow<NetworkResult<ChatsListResponse>>

    fun sendMessage(sendMessageBody: SendMessageBody?): Flow<NetworkResult<ApiResponse2>>
}