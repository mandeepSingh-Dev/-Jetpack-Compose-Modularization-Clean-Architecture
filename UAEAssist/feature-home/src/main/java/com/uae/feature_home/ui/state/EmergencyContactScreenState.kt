package com.uae.feature_home.ui.state

import com.uae.core_common.UIState
import com.uae.feature_home.remote.model.response.ContactsResponse.ContactsData
import com.uae.feature_profile.remote.model.response.ProfileResponse

data class EmergencyContactScreenState(
    val isLoading : Boolean = false,
    val isRefreshing : Boolean = false,
    val isContactAddInProgress : Boolean = false,
    val userData : ProfileResponse.UserData? = null,
    val contactsList: List<ContactsData?>? = emptyList()

) : UIState