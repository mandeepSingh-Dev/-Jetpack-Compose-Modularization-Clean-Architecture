package com.uae.feature_location.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.google.android.gms.maps.model.LatLng
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.HeaderView
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.feature_location.ui.viewmodels.MyLocationViewModel
import com.uae.feature_location.ui.viewmodels.TrackAssistanceViewModel
import kotlinx.coroutines.flow.collectLatest
import okhttp3.internal.http2.Header


@Composable
fun MyLocationScreen(
    snackBarHostState: SnackbarHostState = LocalSnackBarHostState.current,
    backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    myLocationViewModel: MyLocationViewModel = hiltViewModel()
) {


    val uiState by myLocationViewModel.uiState.collectAsStateWithLifecycle()

    BoxCommon(
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false, modifier = Modifier.fillMaxSize()
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            HeaderView(title = "My Location") {
                backStack.removeLastOrNull()
            }


            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMapScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    currentLatLng = uiState?.currentLatLng
                )
            }

        }

    }


}