package com.uae.feature.auth.ui.viewmodel

import com.uae.core.navigation.HomeScreens
import com.uae.core.navigation.ProfileScreens
import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature.auth.domain.usecase.AuthAllUseCases
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody
import com.uae.feature.auth.remote.model.requestBody.VerifyOtpRequestBody
import com.uae.feature.auth.ui.state.OTPScreenState
import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.uae.feature.auth.ui.events.LoginScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class OTPViewModel @Inject constructor(
    @ApplicationContext val  context : Context,
    private val authAllUseCases: AuthAllUseCases
) : BaseViewModel<OTPScreenState>(OTPScreenState()){


    fun verifyOTP(otp : String?){

        Log.d("kfnbkfnb", otp.toString())

        if(otp.isNullOrEmpty()){
            onEvent(CommonUiEvent.ShowError("Please enter correct OTP!"))
            return
        }else {
            val body = VerifyOtpRequestBody(phoneOtp = otp)
            authAllUseCases.verifyOTPUseCase(verifyOtpRequestBody = body).onEach { networkResult ->

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
                        onEvent(CommonUiEvent.ShowSuccessMessage(networkResult.data?.message))
                        if(networkResult.data?.data?.setProfile == 0){
                            onEvent(CommonUiEvent.NavigateTo(ProfileScreens.ProfileSetUpScreen))
                        }else{
                            onEvent(CommonUiEvent.NavigateTo(HomeScreens.HomeScreen))
                        }
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
        }else{
            null
        }
    }
}