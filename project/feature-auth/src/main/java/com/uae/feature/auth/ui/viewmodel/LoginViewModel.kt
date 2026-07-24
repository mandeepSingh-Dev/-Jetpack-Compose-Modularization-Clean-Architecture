package com.uae.feature.auth.ui.viewmodel

import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature.auth.domain.usecase.AuthAllUseCases
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody
import com.uae.feature.auth.ui.events.LoginScreenEvent
import com.uae.feature.auth.ui.state.LoginState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authAllUseCases: AuthAllUseCases
) : BaseViewModel<LoginState>(LoginState()){


    fun login(){

        val body = uiState.value?.loginRequestBody
        val error = validateFields(body)
        if(error != null){
            onEvent(CommonUiEvent.ShowError(error))
            return
        }else {
            authAllUseCases.loginUseCase(loginRequestBody = body).onEach { networkResult ->

                when (networkResult) {
                    is NetworkResult.Loading -> {
                        updateState { state ->
                            state?.copy(isLoading = true)
                        }
                    }

                    is NetworkResult.Success -> {
                        updateState { state ->
                            state?.copy(isLoading = false)
                        }
                        onEvent(LoginScreenEvent.LoginSuccess)
                    }

                    is NetworkResult.Error -> {
                        updateState { state ->
                            state?.copy(isLoading = false)
                        }
                        onEvent(CommonUiEvent.ShowError(networkResult.error))
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun validateFields(loginRequestBody: LoginRequestBody?): String? {

        return if(loginRequestBody?.phone.isNullOrEmpty()){
         "Please enter your phone number"
        }else if(loginRequestBody.phone.any { !it.isDigit() }){
            "Please enter correct phone number"
        }else if(uiState.value?.isTermsConditionsChecked == false){
            "Please accept the Terms & Conditions and Privacy Policy to continue."
        }else{
            null
        }
    }
}