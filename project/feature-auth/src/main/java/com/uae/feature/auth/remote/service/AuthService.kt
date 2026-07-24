package com.uae.feature.auth.remote.service

import com.uae.core_network.networkUtils.EndPoints
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody
import com.uae.feature.auth.remote.model.requestBody.ProfileSetupRequestBody
import com.uae.feature.auth.remote.model.requestBody.VerifyOtpRequestBody
import com.uae.feature.auth.remote.model.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthService {


    @POST(EndPoints.Auth.LOGIN)
    suspend fun login(@Body loginRequestBody: LoginRequestBody?) : Response<LoginResponse>

    @POST(EndPoints.Auth.VERIFY_OTP)
    suspend fun verifyOTP(@Body body: VerifyOtpRequestBody?) : Response<LoginResponse>

    @PUT(EndPoints.Auth.PROFILE)
    suspend fun updateProfile(@Body body: ProfileSetupRequestBody?) : Response<LoginResponse>


}