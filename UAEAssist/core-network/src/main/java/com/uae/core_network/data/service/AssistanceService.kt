package com.uae.core_network.data.service

import com.uae.core_network.data.model.AssistanceListResponse
import com.uae.core_network.data.model.LastAcceptedAssistanceResponse
import com.uae.core_network.data.model.requestBody.AssistanceClickRequestBody
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AssistanceService {


    @GET(EndPoints.Assistance.ASSISTANCE)
    suspend fun assistanceListing(
        @Query(ApiConstants.page) page : Int = 1,
        @Query(ApiConstants.limit) limit : Int = 10,
        @Query(ApiConstants.search) search : String?,
        @Query(ApiConstants.status) status : Int? = 1,
    ) : Response<AssistanceListResponse>

    @POST(EndPoints.Assistance.ASSISTANCE)
    suspend fun assistanceClick(
        @Body assistanceClickRequestBody : AssistanceClickRequestBody?
    ) : Response<ApiResponse2>


    @GET(EndPoints.Assistance.LAST_ACCEPTED)
    suspend fun getLastAcceptedAssistance(): Response<LastAcceptedAssistanceResponse>


}