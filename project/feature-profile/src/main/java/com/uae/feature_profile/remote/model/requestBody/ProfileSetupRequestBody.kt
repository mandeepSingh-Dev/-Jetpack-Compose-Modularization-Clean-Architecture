package com.uae.feature_profile.remote.model.requestBody


data class ProfileSetupRequestBody(
    val about: String? = null,
    val birthDate: String?= null,
    val bloodGroup: String?= null,
    val email: String?= null,
    val firstName: String?= null,
    val gender: Int?= null,
    val lastName: String?= null,
    val medicalCondtion: String?= null,
    val profilePic: String?= null,
    val setProfile: Int?= null
)