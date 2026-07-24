package com.uae.feature_home.remote.model.response


import com.google.gson.annotations.SerializedName

data class FaqsListResponse(
    val count: Int?,
    val `data`: List<FaqsData?>?,
    val message: String?,
    val status: Int?
) {
    data class FaqsData(
        val active: Int?,
        val category: Any?,
        val createdAt: String?,
        val description: String?,
        @SerializedName("_id")
        val id: String?,
        val title: String?,
        val updatedAt: String?,
        val isVisible : Boolean = false
    )
}