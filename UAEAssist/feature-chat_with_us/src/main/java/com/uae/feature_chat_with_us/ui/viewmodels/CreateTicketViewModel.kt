package com.uae.feature_chat_with_us.ui.viewmodels

import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_chat_with_us.domain.usecase.ChatWithUsAllUseCases
import com.uae.feature_chat_with_us.remote.model.requestBody.CreateTickerRequestBody
import com.uae.feature_chat_with_us.ui.states.CreateTicketScreenState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class CreateTicketViewModel @Inject constructor(private val chatWithUsAllUseCases: ChatWithUsAllUseCases): BaseViewModel<CreateTicketScreenState>(CreateTicketScreenState()) {


    fun createTicket(){
        val body = uiState.value?.createTickerRequestBody

        val error = validateFields(body)
        if(!error.isNullOrEmpty()){
            onEvent(CommonUiEvent.ShowError(error))
        }else{
            chatWithUsAllUseCases.createTicketUseCase(createTickerRequestBody = body)?.onEach { networkResult ->
                when(networkResult){
                    is NetworkResult.Loading -> {
                        updateState { state -> state?.copy(isLoading = true) }
                    }
                    is NetworkResult.Error -> {
                        updateState { state -> state?.copy(isLoading = false) }
                    }
                    is NetworkResult.Success -> {
                        updateState { state -> state?.copy(isLoading = false) }
                    }
                }
            }?.launchIn(viewModelScope)
        }

    }

    fun validateFields(body: CreateTickerRequestBody?): String? {
        return if(body?.title.isNullOrEmpty()){
            "Please enter ticket title"
        }else if(body.description.isNullOrEmpty()){
            "Please enter ticket description"
        }else{
            null
        }
    }

}