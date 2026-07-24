package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName

data class CMSResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val createdAt: String?,
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        val role: Int?,
        val type: Int?,
        val updatedAt: String?,
        @SerializedName("__v")
        val v: Int?
    )
}