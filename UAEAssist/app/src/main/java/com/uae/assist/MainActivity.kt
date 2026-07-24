package com.uae.assist

import com.uae.core.navigation.AppScreens
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.core_common.local_providers.LocalSnackBarHostState
import com.uae.core_common.theme.theme_color_1
import com.uae.core_common.theme.theme_color_2
import com.uae.feature.auth.navigation.authGraph
import com.uae.feature_chat_with_us.navigation.chatWithUsGraph
import com.uae.feature_home.navigation.homeGraph
import com.uae.feature_profile.navigation.profileGraph
import com.uae.assist.ui.screens.SplashScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.google.firebase.messaging.FirebaseMessaging
import com.uae.feature_location.navigation.locationGraph
import com.zodiaq.ui.theme.Shape_20
import com.zodiaq.ui.theme.UAEAssistTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UAEAssistTheme {
                val backstack = rememberNavBackStack(AppScreens.SplashScreen)
                val snackBarHostState = remember { SnackbarHostState() }
                CompositionLocalProvider(
                    LocalSnackBarHostState provides snackBarHostState,
                    LocalBackStackNav provides backstack,
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(snackBarHostState) {
                                Snackbar(
                                    shape = Shape_20,
                                    snackbarData = it,
                                    dismissActionContentColor = theme_color_1,
                                    containerColor = theme_color_1,
                                    contentColor = Color.White,
                                    actionColor = theme_color_2,
                                    actionOnNewLine = true,
                                )
                            }

                        },modifier = Modifier.fillMaxSize()) { innerPadding ->
                    innerPadding


                        NavDisplay(
                            backStack = backstack,
                            onBack = {
                                if (backstack.size > 1) backstack.removeLastOrNull()
                            },
                            entryDecorators = listOf(
                                rememberSaveableStateHolderNavEntryDecorator(),
                                rememberViewModelStoreNavEntryDecorator() // This enables per-screen ViewModels
                            ),
                            entryProvider = entryProvider {

                                entry<AppScreens.SplashScreen> {
                                    SplashScreen {
                                        backstack.removeAll(backstack)
                                        backstack.add(it)
                                    }
                                }
                                authGraph(backstack)
                                profileGraph(backstack)
                                homeGraph(backstack)
                                chatWithUsGraph(backstack)
                                locationGraph(backstack)
//                            HomeGraph()



                            })
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UAEAssistTheme {
        Greeting("Android")
    }

}

