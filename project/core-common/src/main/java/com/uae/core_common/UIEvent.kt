package com.uae.core_common

import androidx.navigation.NavOptions
import androidx.navigation3.runtime.NavKey

interface UIEvent

sealed interface CommonUiEvent : UIEvent{
    data class ShowError(val error: String?, val errorCode : Int? = null, val isSnackBar : Boolean = true) :
        CommonUiEvent {
        constructor(errorCode : Int) : this(error = null, errorCode = errorCode)
    }
    data class ShowSuccessMessage(val message: String?) : CommonUiEvent
    data class NavigateTo(val routeNavKey : NavKey, val navOptions : NavOptions? = null) :
        CommonUiEvent

}