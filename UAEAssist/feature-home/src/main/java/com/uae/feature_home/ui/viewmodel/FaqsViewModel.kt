package com.uae.feature_home.ui.viewmodel

import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.domain.usecase.HomeAllUseCases
import com.uae.feature_home.ui.state.FaqsScreenState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class FaqsViewModel  @Inject constructor(
    private val homeAllUseCases: HomeAllUseCases
): BaseViewModel<FaqsScreenState>(FaqsScreenState()){


    init {
        homeAllUseCases.getFaqsUseCase().onEach { networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> {
                    updateState { state ->
                        state?.copy(isLoading = true)
                    }
                }
                is NetworkResult.Success -> {
                    updateState { state ->
                        state?.copy(isLoading = false, faqsList = networkResult.data?.data)
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