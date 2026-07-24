package com.uae.feature_home.remote.repository

import com.uae.core_common.UserManager
import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.NetworkHelper
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.networkUtils.handleUseCaseException
import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.remote.model.requestBody.AddContactRequestBody
import com.uae.feature_home.remote.model.requestBody.RatingRequestBody
import com.uae.feature_home.remote.model.response.AddContactResponse
import com.uae.feature_home.remote.model.response.CategoryListingResponse
import com.uae.feature_home.remote.model.response.ContactsResponse
import com.uae.feature_home.remote.model.response.FaqsListResponse
import com.uae.feature_home.remote.model.response.NotificationListResponse
import com.uae.feature_home.remote.model.response.SubCategoriesListResponse
import com.uae.feature_home.remote.service.HomeService
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService,
    private val networkHelper: NetworkHelper,
    private val userManager: UserManager
) : HomeRepository {

    override fun getCategoryListing(
        page: Int,
        limit: Int,
        search: String?,
        active: Int?
    ): Flow<NetworkResult<CategoryListingResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<CategoryListingResponse>(
                call = {
                    homeService.categoryListing(
                        page = page,
                        limit = limit,
                        search = search,
                        active = active
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun getSubCategoryListing(
        page: Int, limit: Int, search: String?, active: Int?,
        categoryId: String
    ): Flow<NetworkResult<SubCategoriesListResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<SubCategoriesListResponse>(
                call = {
                    homeService.subCategoryListing(
                        page = page,
                        limit = limit,
                        search = search,
                        active = active,
                        categoryId = categoryId
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }




    override fun addContact(addContactRequestBody: AddContactRequestBody?): Flow<NetworkResult<AddContactResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<AddContactResponse>(
                call = {
                    homeService.addContact(
                        addContactRequestBody = addContactRequestBody
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun getContacts(): Flow<NetworkResult<ContactsResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<ContactsResponse>(
                call = {
                    homeService.getContacts()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun deleteContact(id: String): Flow<NetworkResult<ApiResponse2>> {
        val body = mapOf(ApiConstants.id to id)
        return try {
            networkHelper.executeWithRetryFlow<ApiResponse2>(
                call = {
                    homeService.deleteContact(body)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }

    override fun editContact(addContactRequestBody : AddContactRequestBody?): Flow<NetworkResult<AddContactResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<AddContactResponse>(
                call = {
                    homeService.editContact(addContactRequestBody = addContactRequestBody)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun addRating(ratingRequestBody : RatingRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return try {
            networkHelper.executeWithRetryFlow<ApiResponse2>(
                call = {
                    homeService.addRating(ratingRequestBody = ratingRequestBody)
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getFaqs(): Flow<NetworkResult<FaqsListResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<FaqsListResponse>(
                call = {
                    homeService.getFaqs()
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
    override fun getNotifications(page : Int,  limit : Int): Flow<NetworkResult<NotificationListResponse>> {
        return try {
            networkHelper.executeWithRetryFlow<NotificationListResponse>(
                call = {
                    homeService.getNotifications(
                        page = page,
                        limit = limit
                    )
                },
                onSuccess = { response ->
                    null
                },
                onError = {
                },
                shouldLoading = false
            )
        } catch (e: Exception) {
            flow {
                emit(NetworkResult.Error(handleUseCaseException(e)))
            }
        }
    }
}

