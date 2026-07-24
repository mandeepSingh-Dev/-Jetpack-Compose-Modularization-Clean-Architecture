package com.uae.feature_chat_with_us.navigation

import com.uae.core.navigation.ChatWithUsScreens
import com.uae.feature_chat_with_us.ui.screens.ChatScreen
import com.uae.feature_chat_with_us.ui.screens.ChatWithUsScreen
import com.uae.feature_chat_with_us.ui.screens.CreateTickerScreen
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey


fun EntryProviderScope<NavKey>.chatWithUsGraph(backstack: NavBackStack<NavKey>) {

    entry<ChatWithUsScreens.ChatWithUsScreen> {
        ChatWithUsScreen()
    }

    entry<ChatWithUsScreens.CreateTickerScreen> {
        CreateTickerScreen()
    }
    entry<ChatWithUsScreens.ChatScreen> {key ->
        ChatScreen(id = key.id, chatType = key.chatType)
    }

}



