package com.uae.feature_chat_with_us.remote.model.requestBody


data class SendMessageBody(
    val chatId: String? = null,
    val chatType: String? = null,
    val messageType: String? = null,
    val message: String? = null,
    val fileName: String? = null,
    val size: String? = null,
    val url: String? = null,
    val mimeType: String? = null,
    @Transient
    val fileUri : String? = null
)

