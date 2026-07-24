package com.uae.assist.ui.screens

import com.uae.core_common.CommonUiEvent
import com.uae.core_common.components.AttachLifecycleObserver
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.theme.theme_color_1
import com.uae.assist.R
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.uae.core_common.utils.ObserveUiEvent
import com.uae.assist.ui.viewmodels.SplashViewModel
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation3.runtime.NavKey
import java.util.Calendar

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navigate : (NavKey) -> Unit) {


    AttachLifecycleObserver() {lifecycleEvent ->
        when(lifecycleEvent){
            Lifecycle.Event.ON_START -> {
                splashViewModel.startNavigation()
            }
            Lifecycle.Event.ON_STOP -> {
                splashViewModel.stopNavigation()
            }
            else -> Unit
        }
    }


    ObserveUiEvent(splashViewModel.uiEvent) {
        when(it) {
            is CommonUiEvent.NavigateTo -> {
                navigate(it.routeNavKey)
            }
        }
    }


    BoxCommon(containerColor = theme_color_1,
        contentAlignment = Alignment.Center,
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false) {
        Image(painter = painterResource(R.drawable.logo_icon), contentDescription = null,
            modifier = Modifier.align(Alignment.Center))

        val year = Calendar.getInstance().get(Calendar.YEAR)

        Text(stringResource(R.string.uae_assist,
            year),
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter).navigationBarsPadding())
    }

}
