package com.uae.core_network.data.repositoryImpl

import com.uae.core_common.UserManager
import com.uae.core_network.data.model.BloodGroupsListResponse
import com.uae.core_network.data.model.CMSResponse
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.data.model.MedicalConditionsListResponse
import com.uae.core_network.data.service.CommonService
import com.uae.core_network.domain.CommonRepository
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody


class CommonRepositoryImpl @Inject constructor(
    private val commonService: CommonService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : CommonRepository {

    override fun getBloodGroups(): Flow<NetworkResult<BloodGroupsListResponse>> {
        return try {
            networkHelper.executeWithFlow<BloodGroupsListResponse>(
                call = {
                    commonService.getBloodGroup()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getMedicalConditions(): Flow<NetworkResult<MedicalConditionsListResponse>> {
        return try {
            networkHelper.executeWithFlow<MedicalConditionsListResponse>(
                call = {
                    commonService.getMedicalConditions()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun uploadImage(file : MultipartBody.Part, folder : RequestBody): Flow<NetworkResult<ImageUploadResponse>> {
        return try {
            networkHelper.executeWithFlow<ImageUploadResponse>(
                call = {
                    commonService.uploadImage(files = file,folderName = folder)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun logout(): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    commonService.logout()
                },
                onSuccess = { response ->
                    userManager.clearLocalData()
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun updateFCMToken(fcmToken : String?): Flow<NetworkResult<ApiResponse2>> {
        return try {
            val body = hashMapOf(ApiConstants.fcmToken to fcmToken)
            networkHelper.executeWithFlow<ApiResponse2>(
                call = {
                    commonService.updateFcmToken(body = body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getCMS(type : Int): Flow<NetworkResult<CMSResponse>> {
        return try {
            networkHelper.executeWithFlow<CMSResponse>(
                call = {
                    commonService.getCMS(type = type)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                }
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}