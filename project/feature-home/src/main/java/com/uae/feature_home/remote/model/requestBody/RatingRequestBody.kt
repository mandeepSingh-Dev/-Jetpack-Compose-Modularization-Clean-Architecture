package com.uae.feature_home.remote.model.requestBody


data class RatingRequestBody(
    val comment: String? = null,
    val rating: Int?= null,
    val requestId: String?= null
)