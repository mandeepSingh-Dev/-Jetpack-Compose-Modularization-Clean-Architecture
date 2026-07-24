package com.uae.feature_chat_with_us.remote.service

import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import com.uae.feature_chat_with_us.remote.model.requestBody.CreateTickerRequestBody
import com.uae.feature_chat_with_us.remote.model.requestBody.SendMessageBody
import com.uae.feature_chat_with_us.remote.model.response.ChatSupportTicketsListResponse
import com.uae.feature_chat_with_us.remote.model.response.ChatsListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ChatWithUsService {


    @GET(EndPoints.HelpSupport.SUPPORT)
    suspend fun getChatWithUsList(
        @Query(ApiConstants.page) page : Int = 1,
        @Query(ApiConstants.limit) limit : Int = 1,
        @Query(ApiConstants.status) status : Int = 1,
    ) : Response<ChatSupportTicketsListResponse>


    @POST(EndPoints.HelpSupport.SUPPORT)
    suspend fun createTicket(
        @Body createTickerRequestBody: CreateTickerRequestBody?
    ) : Response<ApiResponse2>



    @POST(EndPoints.HelpSupport.CHAT_MESSAGES)
    suspend fun sendMessage(
        @Body sendMessageBody: SendMessageBody?
    ) : Response<ApiResponse2>


    @GET(EndPoints.HelpSupport.CHAT_MESSAGES)
    suspend fun getChatsList(
        @Query(ApiConstants.id) id : String?,
        @Query(ApiConstants.page) page : Int? = 1,
        @Query(ApiConstants.limit) limit : Int? = 20,
    ) : Response<ChatsListResponse>
}