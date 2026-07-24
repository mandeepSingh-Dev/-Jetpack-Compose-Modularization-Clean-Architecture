package com.uae.core_network.data.model.requestBody

data class AssistanceClickRequestBody(
    val category: String?,
    val subCategory: String? = null,
    val type : String?
)