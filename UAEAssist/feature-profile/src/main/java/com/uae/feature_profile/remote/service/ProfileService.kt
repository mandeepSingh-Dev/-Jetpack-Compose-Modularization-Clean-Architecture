package com.uae.feature_profile.remote.service

import com.uae.core_network.networkUtils.EndPoints
import com.uae.feature_profile.remote.model.requestBody.ProfileSetupRequestBody
import com.uae.feature_profile.remote.model.response.ProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileService {



    @PUT(EndPoints.Auth.PROFILE)
    suspend fun updateProfile(@Body body: ProfileSetupRequestBody?) : Response<ProfileResponse>


    @GET(EndPoints.Auth.PROFILE)
    suspend fun getProfile() : Response<ProfileResponse>




}