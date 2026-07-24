package com.uae.feature_chat_with_us.ui.utils

enum class ChatSupportType(val type : Int) {
    OPEN(0),
    COMPLETED(1);

    companion object{
        fun default() = OPEN
    }
}