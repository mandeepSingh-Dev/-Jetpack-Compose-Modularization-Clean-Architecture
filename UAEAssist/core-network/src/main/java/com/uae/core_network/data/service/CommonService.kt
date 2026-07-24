package com.uae.core_network.data.service

import com.uae.core_network.data.model.BloodGroupsListResponse
import com.uae.core_network.data.model.CMSResponse
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.MedicalConditionsListResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query


interface CommonService {

    @GET(EndPoints.Common.BLOOD_GROUPS)
    suspend fun getBloodGroup() : retrofit2.Response<BloodGroupsListResponse>
    @GET(EndPoints.Common.MEDICAL_CONDITIONS)
    suspend fun getMedicalConditions() : retrofit2.Response<MedicalConditionsListResponse>


    @Multipart
    @POST(EndPoints.Common.IMAGE_UPLOAD)
    suspend fun uploadImage(
        @Part files: MultipartBody.Part,
        @Part(ApiConstants.folderName) folderName: RequestBody
    ): Response<ImageUploadResponse>


    @POST(EndPoints.Auth.LOGOUT)
    suspend fun logout(): Response<ApiResponse2>



    @PUT(EndPoints.Common.FCM_TOKEN)
    suspend fun updateFcmToken(
        @Body body : Map<String, String?>
    ): Response<ApiResponse2>


    @GET(EndPoints.Common.CMS)
    suspend fun getCMS(
        @Query(ApiConstants.type) type : Int?,
        @Query(ApiConstants.role) role : Int = 1,
    ): Response<CMSResponse>



}