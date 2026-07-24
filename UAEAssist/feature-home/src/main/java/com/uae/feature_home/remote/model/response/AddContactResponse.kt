package com.uae.feature_home.remote.model.response


import com.google.gson.annotations.SerializedName

data class AddContactResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val countryCode: String?,
        val createdAt: String?,
        val fullName: String?,
        @SerializedName("_id")
        val id: String?,
        val phone: String?,
        val updatedAt: String?,
        val userId: String?
    )
}