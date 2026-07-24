package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName

data class BloodGroupsListResponse(
    val count: Int?,
    val `data`: List<BloodGroup?>?,
    val message: String?,
    val status: Int?
) {
    data class BloodGroup(
        val active: Int?,
        @SerializedName("_id")
        val id: String?,
        val name: String?
    )
}