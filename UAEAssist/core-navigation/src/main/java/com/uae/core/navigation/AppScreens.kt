package com.uae.core.navigation

import android.location.Location
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreens : NavKey{

    @Serializable
    object SplashScreen : AppScreens

}

@Serializable
sealed interface AuthScreens : NavKey {
    @Serializable
    data object LoginScreen : AuthScreens
    @Serializable
    data class OTPScreen(val loginBody : String?) : AuthScreens
}

@Serializable
sealed interface HomeScreens : NavKey {

    @Serializable
    data object HomeScreen : HomeScreens

    @Serializable
    data class SubCategoriesScreen(val categoryId : String, val categoryName : String, val subCategoriesListData : String?) : HomeScreens
    @Serializable
    data object AssistanceScreen : HomeScreens
    @Serializable
    data object EmergencyContactsScreen : HomeScreens


    @Serializable
    data class RateUsScreen(val requestId : String) : HomeScreens

    @Serializable
    data object  FaqScreen : HomeScreens
    @Serializable
    data object  NotificationScreen : HomeScreens

    @Serializable
    data class  TermsConditionsScreen (val type :Int): HomeScreens
}


@Serializable
sealed interface ProfileScreens : NavKey {
    @Serializable
    data object ProfileSetUpScreen : ProfileScreens
}


@Serializable
sealed interface ChatWithUsScreens : NavKey {
    @Serializable
    data object ChatWithUsScreen : ChatWithUsScreens
    @Serializable
    data object CreateTickerScreen : ChatWithUsScreens
    @Serializable
    data class ChatScreen(val id : String?, val chatType : String?) : ChatWithUsScreens
}



@Serializable
sealed interface LocationScreens : NavKey {
    @Serializable
    data class TrackAssistanceScreen(val trackAssistanceDataJson : String?) : LocationScreens
    @Serializable
    data object MyLocationScreen : LocationScreens


}