package com.uae.feature_chat_with_us.ui.event

import com.uae.core_common.UIEvent
import com.uae.feature_chat_with_us.remote.model.requestBody.SendMessageBody

sealed interface ChatScreenEvent : UIEvent{

    data class SendMessage(val sendMessageBody: SendMessageBody?) : ChatScreenEvent
    data class SendMediaMessage(val sendMessageBody: SendMessageBody?) : ChatScreenEvent
}