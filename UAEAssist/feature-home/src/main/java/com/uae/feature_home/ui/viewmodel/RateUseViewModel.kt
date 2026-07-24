package com.uae.feature_home.ui.viewmodel

import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.domain.usecase.HomeAllUseCases
import com.uae.feature_home.remote.model.requestBody.RatingRequestBody
import com.uae.feature_home.ui.events.RateUsScreenEvents
import com.uae.feature_home.ui.state.RateUsScreenState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class RateUseViewModel @Inject constructor(
    private val homeAllUseCases: HomeAllUseCases
): BaseViewModel<RateUsScreenState>(RateUsScreenState()){



    fun addRating(){
        val body = uiState.value?.ratingRequestBody
        val error = validateFields(body)
        if(!error.isNullOrEmpty()){
            onEvent(CommonUiEvent.ShowError(error))
            return
        }else {
            homeAllUseCases.addRatingUseCase(ratingRequestBody = body).onEach { networkResult ->
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
                        onEvent(RateUsScreenEvents.RatingAddedSuccessfully)
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


    fun validateFields(body: RatingRequestBody?): String? {

        return if(body?.comment.isNullOrEmpty()){
            "Please enter your comments."
        }else{
            null
        }
    }

}