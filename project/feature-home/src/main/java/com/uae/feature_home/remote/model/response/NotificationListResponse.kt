package com.uae.feature_home.remote.model.response


import com.google.gson.annotations.SerializedName

data class NotificationListResponse(
    val count: Int?,
    val `data`: List<NotificationData>?,
    val message: String?,
    val status: Int?
) {
    data class NotificationData(
        val createdAt: String?,
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        val isRead: Boolean?,
        val notificationData: NotificationData?,
        val title: String?,
        val updatedAt: String?,
        val userId: String?,
        @SerializedName("__v")
        val v: Int?
    ) {
        data class NotificationData(
            val fullName: String?,
            val notificationType: Int?,
            val profilePic: String?,
            val userId: String?
        )
    }
}