package com.uae.feature.auth.remote.model.requestBody


data class ProfileSetupRequestBody(
    val about: String?,
    val birthDate: String?,
    val bloodGroup: Any?,
    val email: String?,
    val firstName: String?,
    val gender: Int?,
    val lastName: String?,
    val medicalCondtion: Any?,
    val profilePic: String?
)