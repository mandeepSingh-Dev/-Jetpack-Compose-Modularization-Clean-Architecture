package com.uae.feature.auth.ui.state

import com.uae.core_common.UIState
import com.uae.core_network.data.model.BloodGroupsListResponse.BloodGroup
import com.uae.core_network.data.model.MedicalConditionsListResponse

data class ProfileSetupScreenState(
    val isLoading : Boolean = false,
    val bloodGroups  : List<BloodGroup?>? = emptyList(),
    val medicalCondition  : List<MedicalConditionsListResponse.MedicalCondition?>? = emptyList(),
    val selectedBloodGroup : BloodGroup? = null,
    val selectedMedicalCondition : MedicalConditionsListResponse.MedicalCondition? = null
) : UIState