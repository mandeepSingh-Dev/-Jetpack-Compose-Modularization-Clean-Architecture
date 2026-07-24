package com.uae.core_network.data.model


import com.google.gson.annotations.SerializedName

data class MedicalConditionsListResponse(
    val count: Int?,
    val `data`: List<MedicalCondition?>?,
    val message: String?,
    val status: Int?
) {
    data class MedicalCondition(
        val active: Int?,
        @SerializedName("_id")
        val id: String?,
        val name: String?
    )
}