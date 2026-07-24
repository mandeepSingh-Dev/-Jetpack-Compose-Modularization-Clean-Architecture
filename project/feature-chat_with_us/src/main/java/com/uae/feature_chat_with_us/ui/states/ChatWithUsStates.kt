package com.uae.feature_chat_with_us.ui.states

import com.uae.core_common.UIState

data class ChatWithUsStates (
    val isLoading : Boolean = false,
    val isOpenedListRefreshing : Boolean = false,
    val isCompletedListRefreshing : Boolean = false,
) : UIState