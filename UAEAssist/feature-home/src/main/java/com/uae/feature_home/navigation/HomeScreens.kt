package com.uae.feature_home.navigation

import com.uae.core.navigation.HomeScreens
import com.uae.core_common.utils.fromJson
import com.uae.feature_home.remote.model.response.SubCategoriesListResponse
import com.uae.feature_home.ui.screens.AssistanceScreen
import com.uae.feature_home.ui.screens.EmergencyContactScreen
import com.uae.feature_home.ui.screens.FaqScreen
import com.uae.feature_home.ui.screens.HomeScreen
import com.uae.feature_home.ui.screens.NotificationScreen
import com.uae.feature_home.ui.screens.RateUsScreen
import com.uae.feature_home.ui.screens.SubCategoriesScreen
import android.util.Log
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.uae.feature_home.ui.screens.TermsConditionsScreen


fun EntryProviderScope<NavKey>.homeGraph(backstack: NavBackStack<NavKey>) {

    entry<HomeScreens.HomeScreen> {
        HomeScreen()
    }
    entry<HomeScreens.SubCategoriesScreen> {key ->

        Log.d("kfnbjkjfnb", key.subCategoriesListData.toString())

        Log.d("fkbnfjbnf",key.subCategoriesListData?.fromJson<List<SubCategoriesListResponse.SubCategoryData?>>().toString())

//        key.subCategoriesListData.fromJson<>()
        SubCategoriesScreen(categoryId = key.categoryId,
            categoryName = key.categoryName,
            subCategoriesList = key.subCategoriesListData?.fromJson<List<SubCategoriesListResponse.SubCategoryData>>() ?: emptyList()
            )

    }


    entry<HomeScreens.AssistanceScreen> {
        AssistanceScreen()
    }

    entry<HomeScreens.EmergencyContactsScreen> {
        EmergencyContactScreen()
    }
    entry<HomeScreens.RateUsScreen> {key ->
        RateUsScreen(requestId = key.requestId)
    }
    entry<HomeScreens.FaqScreen> {key ->
        FaqScreen()
    }
    entry<HomeScreens.NotificationScreen> {key ->
        NotificationScreen()
    }
    entry<HomeScreens.TermsConditionsScreen> {key ->
        TermsConditionsScreen(key.type)
    }
}



