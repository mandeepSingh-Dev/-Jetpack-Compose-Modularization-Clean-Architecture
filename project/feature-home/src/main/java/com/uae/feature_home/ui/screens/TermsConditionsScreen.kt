package com.uae.feature_home.ui.screens

import android.webkit.WebView
import android.webkit.WebViewClient
import com.uae.core_common.components.BoxCommon
import com.uae.core_common.components.HeaderView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.uae.core_common.local_providers.LocalBackStackNav
import com.uae.feature_home.ui.viewmodel.TermsConditionsViewModel


@Composable
fun TermsConditionsScreen(
     type : Int = 1,
     backStack: NavBackStack<NavKey> = LocalBackStackNav.current,
    termsConditionsViewModel : TermsConditionsViewModel = hiltViewModel()
) {

    val uiState by termsConditionsViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        termsConditionsViewModel.getCms(type = type)
    }

    BoxCommon(modifier = Modifier.fillMaxSize(),
        isAppearanceLightStatusBars = false,
        isAppearanceLightNavigationBars = false) {

        Column(modifier = Modifier.fillMaxSize()) {

            val title = when(type){
                1 -> "Terms & Conditions"
                2 -> "Privacy Policy"
                3 -> "About Us"
                else -> ""
            }

            HeaderView(
                title = title,
            ){
                backStack.removeLastOrNull()
            }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .navigationBarsPadding()
                        .verticalScroll(rememberScrollState())
                        .padding(15.dp)
                ) {
                    AndroidView(factory = { context ->
                        val htmlContent = uiState?.description ?: ""
                        WebView(context).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            webViewClient = WebViewClient()
                            loadDataWithBaseURL(
                                null, htmlContent, "text/html", "UTF-8", null
                            )
                        }
                    }, update = { webView ->
                        val htmlContent = uiState?.description ?: ""
                        val styledHtml = """
            <html>
                <head>
                    <style>
                        body {
                            font-size: 11px; 
                            line-height: 1.1;
                        }
                    </style>
                </head>
                <body>
                    $htmlContent
                </body>
            </html>
        """.trimIndent()
                        webView.loadDataWithBaseURL(
                            null, styledHtml, "text/html", "UTF-8", null
                        )
                    }, modifier = Modifier.fillMaxSize())
                }
            }
        }

    }
