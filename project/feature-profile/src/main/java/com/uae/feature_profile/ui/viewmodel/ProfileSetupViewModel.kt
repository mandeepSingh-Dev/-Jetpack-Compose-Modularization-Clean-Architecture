package com.uae.feature_profile.ui.viewmodel

import com.uae.core.navigation.HomeScreens
import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_network.data.model.ImageUploadResponse
import com.uae.core_network.domain.CommonUseCases.CommonAllUseCases
import com.uae.core_network.networkUtils.NetworkResult
import com.uae.core_network.utils.FolderName
import com.uae.feature_profile.domain.usecase.ProfileAllUseCases
import com.uae.feature_profile.ui.state.ProfileSetupScreenState
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val profileAllUseCases: ProfileAllUseCases,
    private val commonAllUseCases: CommonAllUseCases,
) : BaseViewModel<ProfileSetupScreenState>(ProfileSetupScreenState()) {


    init {
        getBloodGroups()
        getMedicalConditions()
        profileAllUseCases.getProfileUseCase().launchIn(viewModelScope)
    }

    fun getBloodGroups(){
        commonAllUseCases.getBloodGroupsUseCase().onEach { networkResult ->
            when(networkResult){
                is NetworkResult.Success -> {
                    updateState { state ->
                        state?.copy(bloodGroups = networkResult.data?.data)
                    }
                }
                else -> Unit
            }
        }
            .launchIn(viewModelScope)
    }

    fun getMedicalConditions(){
        commonAllUseCases.getMedicalConditionsUseCase().onEach { networkResult ->
            when(networkResult){
                is NetworkResult.Success -> {
                    updateState { state ->
                        state?.copy(medicalCondition = networkResult.data?.data)
                    }
                }
                else -> Unit
            }
        }
            .launchIn(viewModelScope)
    }


    fun performUpdateProfile() = viewModelScope.launch{
        val profilePic = uiState.value?.profileSetupRequestBody?.profilePic
        if(!profilePic.isNullOrEmpty()){
            uploadImage(fileUri = profilePic, folderName = FolderName.PROFILE_IMAGE_FOLDER.folderName).onEach { networkResult ->
                when(networkResult){
                    is NetworkResult.Loading -> {
                        updateState { state ->
                            state?.copy(isLoading = true)
                        }
                    }
                    is NetworkResult.Success -> {

                        updateState { state ->
                            state?.copy(profileSetupRequestBody = state.profileSetupRequestBody?.copy(profilePic = networkResult.data?.data?.file))
                        }

                        updateProfile(coroutineScope = this)
                    }
                    is NetworkResult.Error -> {
                        updateState { state ->
                            state?.copy(isLoading = false)
                        }
                        onEvent(CommonUiEvent.ShowError(error = networkResult.error))
                    }
                }
            }
                .launchIn(this)
        }else{
            updateProfile(coroutineScope = this)
        }

    }

     fun updateProfile(coroutineScope : CoroutineScope){
        var body = uiState.value?.profileSetupRequestBody
        body = body?.copy(
            bloodGroup = uiState.value?.selectedBloodGroup?.id,
            medicalCondtion = uiState.value?.selectedMedicalCondition?.id,
            setProfile = 1
        )

         profileAllUseCases.profileUpdateUseCase(profileSetupRequestBody = body).onEach { networkResult ->

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
                    onEvent(CommonUiEvent.ShowSuccessMessage(message = networkResult.data?.message))
                    onEvent(CommonUiEvent.NavigateTo(HomeScreens.HomeScreen))
                }
                is NetworkResult.Error -> {
                    updateState { state ->
                        state?.copy(isLoading = false)
                    }
                }
            }
        }.launchIn(coroutineScope)
    }

    fun uploadImage(fileUri : String?,folderName : String?): Flow<NetworkResult<ImageUploadResponse>> {
        return commonAllUseCases.imageUploadUseCase(
            fileUri = fileUri,
            folderName = folderName
        )
    }

}