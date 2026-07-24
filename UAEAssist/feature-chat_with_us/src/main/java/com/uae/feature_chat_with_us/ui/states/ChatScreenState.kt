package com.uae.feature_chat_with_us.ui.states

import com.uae.core_common.UIState

data class ChatScreenState(
     val isLoading : Boolean = false,
     val isMessageSending : Boolean = false,
     val isRefreshing : Boolean = false
) : UIState