package com.uae.feature_home.domain

import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.remote.model.requestBody.AddContactRequestBody
 import com.uae.feature_home.remote.model.requestBody.RatingRequestBody
import com.uae.feature_home.remote.model.response.AddContactResponse
import com.uae.feature_home.remote.model.response.CategoryListingResponse
import com.uae.feature_home.remote.model.response.ContactsResponse
import com.uae.feature_home.remote.model.response.FaqsListResponse
import com.uae.feature_home.remote.model.response.NotificationListResponse
import com.uae.feature_home.remote.model.response.SubCategoriesListResponse
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    fun getCategoryListing(
        page: Int,
        limit: Int,
        search: String?,
        active: Int?
    ): Flow<NetworkResult<CategoryListingResponse>>

    fun getSubCategoryListing(
        page: Int,
        limit: Int,
        search: String?,
        active: Int?,
        categoryId : String
    ): Flow<NetworkResult<SubCategoriesListResponse>>



    fun addContact(addContactRequestBody: AddContactRequestBody?): Flow<NetworkResult<AddContactResponse>>
    fun getContacts(): Flow<NetworkResult<ContactsResponse>>
    fun deleteContact(id: String): Flow<NetworkResult<ApiResponse2>>
    fun editContact(addContactRequestBody : AddContactRequestBody?): Flow<NetworkResult<AddContactResponse>>
    fun addRating(ratingRequestBody: RatingRequestBody?): Flow<NetworkResult<ApiResponse2>>
    fun getFaqs(): Flow<NetworkResult<FaqsListResponse>>
    fun getNotifications(page: Int, limit: Int): Flow<NetworkResult<NotificationListResponse>>
}