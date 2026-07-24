package com.uae.feature_chat_with_us.remote.model.response


import com.google.gson.annotations.SerializedName

data class ChatSupportTicketsListResponse(
    val count: Int?,
    val `data`: List<ChatSupportData>?,
    val message: String?,
    val status: Int?
) {
    data class ChatSupportData(
        val attachments: List<Attachment?>?,
        val createdAt: String?,
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        val newMessageCount: Int?,
        val status: Int?,
        val ticketId: String?,
        val title: String?,
        val updatedAt: String?,
        val user: User?
    ) {
        data class Attachment(
            @SerializedName("_id")
            val id: String?,
            val type: Int?,
            val url: String?
        )

        data class User(
            val fullname: String?,
            @SerializedName("_id")
            val id: String?,
            val profilePic: String?
        )
    }
}