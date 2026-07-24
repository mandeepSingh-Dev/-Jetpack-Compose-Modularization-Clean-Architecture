package com.uae.feature_home.domain.usecase

import com.uae.core_network.models.ApiResponse2
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.domain.HomeRepository
import com.uae.feature_home.remote.model.requestBody.AddContactRequestBody
 import com.uae.feature_home.remote.model.requestBody.RatingRequestBody
import com.uae.feature_home.remote.model.response.AddContactResponse
import com.uae.feature_home.remote.model.response.ContactsResponse
import com.uae.feature_home.remote.model.response.FaqsListResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class HomeAllUseCases @Inject constructor(
     val addContactUseCase : AddContactUseCase,
    val getContactsUseCase : GetContactsUseCase,
    val deleteContactUseCase : DeleteContactUseCase,
    val editContactUseCase : EditContactUseCase,
    val addRatingUseCase : AddRatingUseCase,
    val getFaqsUseCase : GetFaqsUseCase,
)





class AddContactUseCase @Inject constructor(private val homeRepository: HomeRepository){

    operator fun invoke(addContactRequestBody : AddContactRequestBody?): Flow<NetworkResult<AddContactResponse>> {
        return homeRepository.addContact(addContactRequestBody = addContactRequestBody)
    }
}

class GetContactsUseCase @Inject constructor(private val homeRepository: HomeRepository){

    operator fun invoke(): Flow<NetworkResult<ContactsResponse>> {
        return homeRepository.getContacts()
    }
}

class DeleteContactUseCase @Inject constructor(private val homeRepository: HomeRepository){

    operator fun invoke(id : String): Flow<NetworkResult<ApiResponse2>> {
        return homeRepository.deleteContact(id = id)
    }
}

class EditContactUseCase @Inject constructor(private val homeRepository: HomeRepository){
    operator fun invoke(addContactRequestBody : AddContactRequestBody?): Flow<NetworkResult<AddContactResponse>> {
        return homeRepository.editContact(addContactRequestBody = addContactRequestBody)
    }
}

class AddRatingUseCase @Inject constructor(private val homeRepository: HomeRepository){
    operator fun invoke(ratingRequestBody: RatingRequestBody?): Flow<NetworkResult<ApiResponse2>> {
        return homeRepository.addRating(ratingRequestBody = ratingRequestBody)
    }
}

class GetFaqsUseCase @Inject constructor(private val homeRepository: HomeRepository){
    operator fun invoke(): Flow<NetworkResult<FaqsListResponse>> {
        return homeRepository.getFaqs()
    }
}
