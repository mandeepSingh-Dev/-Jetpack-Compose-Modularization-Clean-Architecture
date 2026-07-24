package com.uae.feature_chat_with_us.remote.model.response


import com.google.gson.annotations.SerializedName

data class ChatsListResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val msgData: List<MsgData>?,
        val ticketDetail: TicketDetail?
    ) {
        data class MsgData(
            val createdAt: String?,
            val fileName: Any?,
            @SerializedName("_id")
            val id: String?,
            val isRead: Boolean?,
            val message: String?,
            val messageType: String?,
            val mimeType: Any?,
            val role: Int?,
            val size: Any?,
            val updatedAt: String?,
            val url: Any?,
            val user: User,
            val userId: String?
        )

        data class User(
            val _id: String?,
            val fullname: String?,
            val profilePic: String?,
        )

        data class TicketDetail(
            val attachments: List<Any?>?,
            val description: String?,
            @SerializedName("_id")
            val id: String?,
            val status: Int?,
            val ticketId: String?,
            val title: String?,
            val user: User?
        ) {
            data class User(
                val fullname: String?,
                @SerializedName("_id")
                val id: String?,
                val profilePic: String?
            )
        }
    }
}