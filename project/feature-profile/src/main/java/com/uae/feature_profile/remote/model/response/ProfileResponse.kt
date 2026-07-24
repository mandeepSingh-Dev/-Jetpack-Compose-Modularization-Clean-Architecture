package com.uae.feature_profile.remote.model.response


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val count: Int?,
    val `data`: UserData?,
    val message: String?,
    val status: Int?
) {
    data class UserData(
        val about: String?,
        val active: Int?,
        val address: String?,
        val appVersion: Any?,
        val birth: String?,
        val birthDate: String?,
        val birthTime: Any?,
        val bloodGroup: String?,
        val buildNumber: Any?,
        val city: String?,
        val coordinates: Coordinates?,
        val countryCode: String?,
        val createdAt: String?,
        val delete: Int?,
        val deviceId: String?,
        val deviceType: String?,
        val email: String?,
        val emailOtp: String?,
        val emailVerified: Int?,
        val fcmToken: String?,
        val firstName: String?,
        val fullName: String?,
        val gender: Int?,
        @SerializedName("_id")
        val id: String?,
        val lastLogin: String?,
        val lastName: String?,
        val location: Location?,
        val medicalComplication: String?,
        val membership: Membership?,
        val notificationPreferences: NotificationPreferences?,
        val notifyMe: Int?,
        val password: String?,
        val phone: String?,
        val phoneOtp: String?,
        val phoneVerified: Int?,
        val preference: List<Any?>?,
        val profilePic: String?,
        val role: Int?,
        val setProfile: Int?,
        val socialType: Any?,
        val step: Int?,
        val updatedAt: String?,
        @SerializedName("user_id")
        val userId: String?,
        @SerializedName("__v")
        val v: Int?
    ) {
        data class Coordinates(
            val lat: String?,
            val lng: String?
        )

        data class Location(
            val coordinates: List<Double?>?,
            val type: String?
        )

        data class Membership(
            val endDate: Any?,
            val planId: Any?,
            val startDate: Any?,
            val status: Int?
        )

        data class NotificationPreferences(
            val bookingsAccess: Int?,
            val channels: Channels?,
            val exclusiveOffers: Int?,
            val recommendations: Int?
        ) {
            data class Channels(
                val email: Int?,
                val push: Int?,
                val sms: Int?,
                val whatsapp: Int?
            )
        }
    }
}