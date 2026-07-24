package com.uae.core_network.data.model


data class ImageUploadResponse(
    val count: Int?,
    val `data`: Data?,
    val message: String?,
    val status: Int?
) {
    data class Data(
        val `file`: String?,
        val folderName: String?
    )
}