package com.uae.feature_home.ui.events

import com.uae.core_common.UIEvent

sealed interface EmergencyContactEvents : UIEvent {

    data class ContactAdded(val message : String?) : EmergencyContactEvents
    data class ContactDeleted(val id : String?) : EmergencyContactEvents
}