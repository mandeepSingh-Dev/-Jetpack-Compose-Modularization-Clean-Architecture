package com.uae.core_network.domain

import com.uae.core_network.data.model.BloodGroupsListResponse
import com.uae.core_network.data.model.CMSResponse
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.MedicalConditionsListResponse
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface CommonRepository {


    fun getBloodGroups() : Flow<NetworkResult<BloodGroupsListResponse>>

    fun getMedicalConditions(): Flow<NetworkResult<MedicalConditionsListResponse>>
    fun uploadImage(
        file: MultipartBody.Part,
        folder: RequestBody
    ): Flow<NetworkResult<ImageUploadResponse>>

    fun logout(): Flow<NetworkResult<ApiResponse2>>
    fun updateFCMToken(fcmToken: String?): Flow<NetworkResult<ApiResponse2>>
    fun getCMS(type: Int): Flow<NetworkResult<CMSResponse>>
}