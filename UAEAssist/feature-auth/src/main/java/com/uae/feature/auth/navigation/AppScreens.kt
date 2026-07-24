package com.uae.feature.auth.navigation

import com.uae.core.navigation.AuthScreens
import com.uae.feature.auth.ui.screens.LoginScreen
import com.uae.feature.auth.ui.screens.OtpScreen
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.uae.core_common.utils.fromJson
import com.uae.feature.auth.remote.model.requestBody.LoginRequestBody


fun EntryProviderScope<NavKey>.authGraph(backstack: NavBackStack<NavKey>) {

    entry<AuthScreens.LoginScreen> {
        LoginScreen()
    }
    entry<AuthScreens.OTPScreen> {
        val body = it.loginBody?.fromJson<LoginRequestBody>()
        OtpScreen(body = body)
    }

}