package com.uae.core_network.domain.CommonUseCases

import com.uae.core_network.data.model.BloodGroupsListResponse
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.MedicalConditionsListResponse
import com.uae.core_network.domain.CommonRepository
import com.uae.core_network.extensions.toMultipartBodyPart
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import android.content.Context
import androidx.core.net.toUri
import com.uae.core_network.data.model.CMSResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class CommonAllUseCases @Inject constructor(
    val getBloodGroupsUseCase : GetBloodGroupsUseCase,
    val getMedicalConditionsUseCase : GetMedicalConditionsUseCase,
    val imageUploadUseCase : ImageUploadUseCase,
    val logoutUseCase : LogoutUseCase,
    val updateFcmTokenUseCase : UpdateFcmTokenUseCase,
    val getCMSUseCase : GetCMSUseCase,
)


class GetBloodGroupsUseCase @Inject constructor(private val commonRepository : CommonRepository){
    operator fun invoke(): Flow<NetworkResult<BloodGroupsListResponse>> {
        return commonRepository.getBloodGroups()
    }
}


class GetMedicalConditionsUseCase @Inject constructor(private val commonRepository : CommonRepository){
    operator fun invoke(): Flow<NetworkResult<MedicalConditionsListResponse>> {
        return commonRepository.getMedicalConditions()
    }
}

class LogoutUseCase @Inject constructor(private val commonRepository : CommonRepository){
    operator fun invoke(): Flow<NetworkResult<ApiResponse2>> {
        return commonRepository.logout()
    }
}


class UpdateFcmTokenUseCase @Inject constructor(private val commonRepository : CommonRepository){
    operator fun invoke(fcmToken : String?): Flow<NetworkResult<ApiResponse2>> {
        return commonRepository.updateFCMToken(fcmToken = fcmToken)
    }
}
class GetCMSUseCase @Inject constructor(private val commonRepository : CommonRepository){
    operator fun invoke(type : Int): Flow<NetworkResult<CMSResponse>> {
        return commonRepository.getCMS(type = type)
    }
}

class ImageUploadUseCase @Inject constructor(
    @ApplicationContext val context : Context,
    private val commonRepository : CommonRepository){
    operator fun invoke(fileUri : String?, folderName : String?): Flow<NetworkResult<ImageUploadResponse>> {


        val fileMultiPartBody = fileUri?.toUri()?.let {
            it.toMultipartBodyPart(context = context)
        }
        return if(fileUri.isNullOrEmpty()){
            flowOf(NetworkResult.Error(error = "Something went wrong with File Uri"))
        }else {
            commonRepository.uploadImage(
                file = fileMultiPartBody!!,
                folder = "Customer".toRequestBody("text/plain".toMediaType())
            )
        }
    }
}