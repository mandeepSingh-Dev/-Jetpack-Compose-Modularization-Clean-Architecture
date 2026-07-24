package com.uae.feature_profile.ui.state

import com.uae.core_common.UIState
import com.uae.core_network.data.model.BloodGroupsListResponse.BloodGroup
import com.uae.core_network.data.model.MedicalConditionsListResponse
import com.uae.feature_profile.remote.model.requestBody.ProfileSetupRequestBody

data class ProfileSetupScreenState(
    val isLoading : Boolean = false,
    val profileSetupRequestBody: ProfileSetupRequestBody? = ProfileSetupRequestBody(),
    val bloodGroups  : List<BloodGroup?>? = emptyList(),
    val medicalCondition  : List<MedicalConditionsListResponse.MedicalCondition?>? = emptyList(),
    val selectedBloodGroup : BloodGroup? = null,
    val selectedMedicalCondition : MedicalConditionsListResponse.MedicalCondition? = null
) : UIState