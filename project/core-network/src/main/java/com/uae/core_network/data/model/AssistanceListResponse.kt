package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName

data class AssistanceListResponse(
    val count: Int?,
    val `data`: List<AssistanceData>?,
    val message: String?,
    val status: Int?
) {
    data class AssistanceData(
        val acceptDeadline: String?,
        val arrivalStatus: Int?,
        val assignedStaff: AssignedStaff?,
        val category: Category?,
        val city: String?,
        val createdAt: String?,
        val customerChatId: String?,
        val customerId: String?,
        val declinedStaff: List<Any?>?,
        @SerializedName("_id")
        val id: String?,
        val isRating: Boolean?,
        val requestId: String?,
        val staffChatId: String?,
        val status: Int?,
        val statusLabel: String?,
        val subCategory: SubCategory?,
        val updatedAt: String?,
        val user: User?,
        @SerializedName("__v")
        val v: Int?
    ) {
        data class Category(
            val bgColor: String?,
            @SerializedName("_id")
            val id: String?,
            val imgSrc: String?,
            val name: String?
        )

        data class SubCategory(
            val bgColor: String?,
            @SerializedName("_id")
            val id: String?,
            val imgSrc: String?,
            val name: String?,
            val slug: String?
        )

        data class User(
            val countryCode: String?,
            val firstName: String?,
            @SerializedName("_id")
            val id: String?,
            val lastName: String?,
            val phone: String?,
            val profilePic: String?,
            @SerializedName("user_id")
            val userId: String?
        )

        data class AssignedStaff(
            @SerializedName("_id")
            val id : String?,
            val firstName : String?,
            val lastName : String?,
            val email : String?,
            val phone : String?,
            val countryCode : String?,
            val active : Int?,
        )
    }
}