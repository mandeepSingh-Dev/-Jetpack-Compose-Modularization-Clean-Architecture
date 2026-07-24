package com.uae.feature_profile.navigation

import com.uae.core.navigation.ProfileScreens
import com.uae.feature_profile.ui.screens.ProfileSetUpScreen
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey


fun EntryProviderScope<NavKey>.profileGraph(backstack: NavBackStack<NavKey>) {

    entry<ProfileScreens.ProfileSetUpScreen> {
        ProfileSetUpScreen()
    }

}