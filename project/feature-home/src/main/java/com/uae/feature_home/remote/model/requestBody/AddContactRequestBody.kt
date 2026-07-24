package com.uae.feature_home.remote.model.requestBody


data class AddContactRequestBody(
    val countryCode: String?,
    val fullName: String?,
    val phone: String?,
    val id: String? = null
)