package com.uae.feature_chat_with_us.domain.usecase

import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_chat_with_us.domain.ChatWithUsRepository
import com.uae.feature_chat_with_us.remote.model.requestBody.CreateTickerRequestBody
import com.uae.feature_chat_with_us.remote.model.requestBody.SendMessageBody
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class ChatWithUsAllUseCases @Inject constructor(
 val createTicketUseCase : CreateTicketUseCase,
 val sendMessageUseCase : SendMessageUseCase,
)


class CreateTicketUseCase @Inject constructor(private val chatWithUsRepository: ChatWithUsRepository){

     operator fun invoke(createTickerRequestBody: CreateTickerRequestBody?): Flow<NetworkResult<ApiResponse2>>? {
        return chatWithUsRepository.createTicket(createTickerRequestBody)
    }
 }



class SendMessageUseCase @Inject constructor(private val chatWithUsRepository: ChatWithUsRepository){

     operator fun invoke(sendMessageBody: SendMessageBody?): Flow<NetworkResult<ApiResponse2>>? {
        return chatWithUsRepository.sendMessage(sendMessageBody = sendMessageBody)
    }
 }




