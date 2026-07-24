package com.uae.feature_home.remote.service

import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.ApiConstants
import com.uae.core_network.networkUtils.EndPoints
import com.uae.feature_home.remote.model.requestBody.AddContactRequestBody
import com.uae.feature_home.remote.model.requestBody.RatingRequestBody
import com.uae.feature_home.remote.model.response.AddContactResponse
import com.uae.feature_home.remote.model.response.CategoryListingResponse
import com.uae.feature_home.remote.model.response.ContactsResponse
import com.uae.feature_home.remote.model.response.FaqsListResponse
import com.uae.feature_home.remote.model.response.NotificationListResponse
import com.uae.feature_home.remote.model.response.SubCategoriesListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface HomeService {


    @GET(EndPoints.Home.CATEGORY)
    suspend fun categoryListing(
        @Query(ApiConstants.page) page : Int = 1,
        @Query(ApiConstants.limit) limit : Int = 10,
        @Query(ApiConstants.search) search : String?,
        @Query(ApiConstants.active) active : Int? = 1,
    ) : Response<CategoryListingResponse>



    @GET(EndPoints.Home.SUB_CATEGORY)
    suspend fun subCategoryListing(
        @Query(ApiConstants.page) page : Int = 1,
        @Query(ApiConstants.limit) limit : Int = 10,
        @Query(ApiConstants.search) search : String?,
        @Query(ApiConstants.active) active : Int? = 1,
        @Query(ApiConstants.categoryId) categoryId : String,
    ) : Response<SubCategoriesListResponse>



    @POST(EndPoints.Contact.CONTACT)
    suspend fun addContact(
        @Body addContactRequestBody : AddContactRequestBody?
    ) : Response<AddContactResponse>



    @GET(EndPoints.Contact.CONTACT)
    suspend fun getContacts(
    ) : Response<ContactsResponse>


    @HTTP(method = "DELETE", hasBody = true, path = EndPoints.Contact.CONTACT)
    suspend fun deleteContact(
        @Body map : Map<String, String>
    ) : Response<ApiResponse2>

    @PUT(EndPoints.Contact.CONTACT)
    suspend fun editContact(
        @Body addContactRequestBody : AddContactRequestBody?
    ) : Response<AddContactResponse>



    @POST(EndPoints.Rating.RATING)
    suspend fun addRating(
        @Body ratingRequestBody: RatingRequestBody?
    ) : Response<ApiResponse2>



    @GET(EndPoints.Faq.FAQ)
    suspend fun getFaqs(
    ) : Response<FaqsListResponse>



    @GET(EndPoints.Notification.NOTIFICATION)
    suspend fun getNotifications(
        @Query(ApiConstants.page) page : Int = 1,
        @Query(ApiConstants.limit) limit : Int = 10,
    ) : Response<NotificationListResponse>


}