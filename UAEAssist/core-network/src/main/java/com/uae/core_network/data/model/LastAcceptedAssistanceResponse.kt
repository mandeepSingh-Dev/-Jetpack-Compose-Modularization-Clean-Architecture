package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName

data class LastAcceptedAssistanceResponse(
    val count: Number?,
    val `data`: TrackAssistanceData?,
    val message: String?,
    val status: Number?
)



data class TrackAssistanceData(
    val isOnline: Any?,
    val request: Request?,
    val requestId: String?,
    val staff: Staff?,
    val status: Number?,
    val timestamp: String?
) {
    data class Request(
        val acceptDeadline: String?,
        val arrivalStatus: Number?,
        val assignedStaff: AssignedStaff?,
        val category: Category?,
        val city: String?,
        val createdAt: String?,
        val customerChatId: String?,
        val customerId: String?,
        val declinedStaff: List<Any?>?,
        @SerializedName("_id")
        val id: String?,
        val requestId: String?,
        val staffChatId: String?,
        val status: Number?,
        val statusLabel: String?,
        val subCategory: Any?,
        val updatedAt: String?,
        val user: User?,
        @SerializedName("__v")
        val v: Number?
    ) {
        data class AssignedStaff(
            val active: Number?,
            val countryCode: String?,
            val email: String?,
            val firstName: String?,
            @SerializedName("_id")
            val id: String?,
            val isOnline: Any?,
            val lastName: String?,
            val phone: String?
        )

        data class Category(
            @SerializedName("_id")
            val id: String?,
            val imgSrc: String?,
            val name: String?
        )

        data class User(
            val countryCode: String?,
            val firstName: String?,
            @SerializedName("_id")
            val id: String?,
            val lastName: String?,
            val latitude: Double?,
            val longitude: Double?,
            val phone: String?,
            val profilePic: String?,
            @SerializedName("user_id")
            val userId: String?
        )
    }

    data class Staff(
        val firstName: String?,
        @SerializedName("_id")
        val id: String?,
        val lastName: String?,
        val latitude: Double?,
        val longitude: Double?,
        val phone: String?,
        val profilePic: String?,
        val countryCode: String?,

    )
}