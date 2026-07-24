package com.uae.feature_chat_with_us.remote.model.requestBody


data class CreateTickerRequestBody(
    val attachments: List<Attachment?>? = emptyList(),
    val description: String? = null,
    val title: String? = null
) {
    data class Attachment(
        val type: Int?,
        val url: String?
    )
}