package com.uae.assist.ui.viewmodels

import com.uae.core.navigation.AuthScreens
import com.uae.core.navigation.HomeScreens
import com.uae.core.navigation.ProfileScreens
import com.uae.core_common.BaseViewModel
import com.uae.core_common.CommonUiEvent
import com.uae.core_common.UIState
import com.uae.core_common.UserManager
import com.uae.core_common.utils.Constants
import com.uae.core_common.utils.fromJson
import com.uae.feature_profile.remote.model.response.ProfileResponse
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@HiltViewModel

class SplashViewModel @Inject constructor(
    private val userManager: UserManager
) : BaseViewModel<UIState>(){

    var job : Job? = null

    fun startNavigation() {
        job = viewModelScope.launch {
            delay(Constants.SPLASH_DELAY)

            val userData = userManager.getUserDataString().fromJson<ProfileResponse.UserData>()
            val userToken = userManager.getUserToken()

            val route = if (userToken.isEmpty() || userData == null) {
                AuthScreens.LoginScreen
            } else if (userData.setProfile == 0) {
                ProfileScreens.ProfileSetUpScreen
            } else {
                HomeScreens.HomeScreen
            }
            onEvent(CommonUiEvent.NavigateTo(route))
        }
    }
    fun stopNavigation(){
        job?.cancel()
    }
}