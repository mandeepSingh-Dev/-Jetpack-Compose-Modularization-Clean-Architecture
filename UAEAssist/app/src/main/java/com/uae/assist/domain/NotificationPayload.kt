package com.uae.assist.domain


import com.google.gson.annotations.SerializedName

data class NotificationPayload(
    val description: String?,
    val notificationData: NotificationData?,
    val title: String?,
    val userId: String?
) {
    data class NotificationData(
        val assistanceChat: Any?,
        val fullName: String?,
        val helpSupportChat: Any?,
        val notificationType: Int?,
        val orderId: Any?,
        val profilePic: String?,
        val request: Any?,
        val userId: String?
    )
}