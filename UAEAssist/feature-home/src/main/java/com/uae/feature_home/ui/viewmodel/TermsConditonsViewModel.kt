package com.uae.feature_home.ui.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.uae.core_common.BaseViewModel
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.feature_home.ui.state.CMSScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@HiltViewModel
class TermsConditionsViewModel @Inject constructor(
    @ApplicationContext val  context : Context,
    private val commonAllUseCases: CommonAllUseCases
) : BaseViewModel<CMSScreenState>(CMSScreenState()) {


    fun getCms(type : Int){
        commonAllUseCases.getCMSUseCase(type).onEach { networkResult ->
            when(networkResult){
                is NetworkResult.Loading -> {
                    updateState { state ->
                        state?.copy(isLoading = true)
                    }
                }
                is NetworkResult.Success -> {

                    val description = networkResult.data?.data?.description
                    updateState { state ->
                        state?.copy(isLoading = false, description = description)
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

}