package com.uae.feature.auth.remote.model.requestBody

data class LoginRequestBody(
    val countryCode: String? = null,
    val phone: String?= null
)