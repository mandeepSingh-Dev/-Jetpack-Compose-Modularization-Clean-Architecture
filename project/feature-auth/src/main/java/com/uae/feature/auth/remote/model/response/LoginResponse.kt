package com.uae.feature.auth.remote.model.response


data class LoginResponse(
    val count: Int?,
    val `data`: LoginData?,
    val message: String?,
    val status: Int?
) {
    data class LoginData(
        val setProfile: Int?,
        val step: Int?,
        val token: String?
    )
}