package com.uae.feature_home.ui.viewmodel

import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.domain.usecase.HomeAllUseCases
import com.uae.feature_home.remote.model.requestBody.AddContactRequestBody
import com.uae.feature_home.ui.events.EmergencyContactEvents
import com.uae.feature_home.ui.state.EmergencyContactScreenState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class EmergencyContactViewModel @Inject constructor(
    private val homeAllUseCases: HomeAllUseCases
) : BaseViewModel<EmergencyContactScreenState>(EmergencyContactScreenState()) {


    init {
        getContacts()
    }


    fun getContacts(isRefresh : Boolean = false) {
        homeAllUseCases.getContactsUseCase().onEach { networkResult ->
            when (networkResult) {
                is NetworkResult.Loading -> {
                    updateState { state ->
                        state?.copy(isLoading = !isRefresh, isRefreshing = isRefresh)
                    }
                }

                is NetworkResult.Success -> {
                    updateState { state ->
                        state?.copy(isLoading = false, isRefreshing = false, contactsList = networkResult.data?.data)
                    }
                }

                is NetworkResult.Error -> {
                    updateState { state ->
                        state?.copy(isLoading = false)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addContact(addContactRequestBody: AddContactRequestBody) {

        val error = validateFields(addContactRequestBody)
        if (!error.isNullOrEmpty()) {
            onEvent(CommonUiEvent.ShowError(error))
            return
        } else {
            homeAllUseCases.addContactUseCase(addContactRequestBody).onEach { networkResult ->
                when (networkResult) {
                    is NetworkResult.Loading -> {
                        updateState { state ->
                            state?.copy(isContactAddInProgress = true)
                        }
                    }

                    is NetworkResult.Success -> {
                        updateState { state ->
                            state?.copy(isContactAddInProgress = false)
                        }
                        onEvent(EmergencyContactEvents.ContactAdded(networkResult.data?.message))
                    }

                    is NetworkResult.Error -> {
                        updateState { state ->
                            state?.copy(isContactAddInProgress = false)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    fun editContact(addContactRequestBody: AddContactRequestBody) {

        val error = validateFields(addContactRequestBody)
        if (!error.isNullOrEmpty()) {
            onEvent(CommonUiEvent.ShowError(error))
            return
        } else {
            homeAllUseCases.editContactUseCase(addContactRequestBody).onEach { networkResult ->
                when (networkResult) {
                    is NetworkResult.Loading -> {
                        updateState { state ->
                            state?.copy(isContactAddInProgress = true)
                        }
                    }

                    is NetworkResult.Success -> {
                        updateState { state ->
                            state?.copy(isContactAddInProgress = false)
                        }
                        onEvent(EmergencyContactEvents.ContactAdded(networkResult.data?.message))
                    }

                    is NetworkResult.Error -> {
                        updateState { state ->
                            state?.copy(isContactAddInProgress = false)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    fun deleteContact(id: String) {
        homeAllUseCases.deleteContactUseCase(id).onEach {networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> {
                    updateState { state ->
                        state?.copy(isLoading = true)
                    }
                }
                is NetworkResult.Success -> {
                    updateState { state ->
                        state?.copy(isLoading = false)
                    }

                    updateState { state ->
                        state?.copy(contactsList = state.contactsList?.filterNot { it?.id == id } )
                    }
                    onEvent(EmergencyContactEvents.ContactDeleted(id = id))
                }
                is NetworkResult.Error -> {
                    updateState { state ->
                        state?.copy(isLoading = false)
                    }
                    onEvent(CommonUiEvent.ShowError(error = networkResult.error))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun validateFields(body: AddContactRequestBody): String? {

        return if (body.fullName.isNullOrEmpty()) {
            "Please enter your full name"
        } else if (body.phone.isNullOrEmpty()) {
            "Please enter your phone number"
        } else {
            null
        }
    }


}