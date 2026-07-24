package com.uae.feature_home.remote.model.response


import com.google.gson.annotations.SerializedName

data class CategoryListingResponse(
    val count: Int?,
    val `data`: List<CategoryData>?,
    val message: String?,
    val status: Int?
) {
    data class CategoryData(
        val action: Boolean?,
        val actionItem: ActionItem?,
        val active: Int?,
        val bgColor: String?,
        val bgImgSrc: String?,
        @SerializedName("_id")
        val id: String?,
        val imgSrc: String?,
        val name: String?,
        val subCategories: List<SubCategoriesListResponse.SubCategoryData?>?,
        val updatedAt: String?
    ) {
        data class ActionItem(
            val countryCode: String?,
            val id: String?,
            val link: String?,
            val phone: String?,
            val type: String?
        )

        data class SubCategory(
            val action: Boolean?,
            val actionItem: ActionItem?,
            val active: Int?,
            val bgColor: String?,
            val bgImgSrc: String?,
            val category: String?,
            val createdAt: String?,
            @SerializedName("_id")
            val id: String?,
            val imgSrc: String?,
            val name: String?,
            val slug: String?,
            val updatedAt: String?,
            @SerializedName("__v")
            val v: Int?
        ) {
            data class ActionItem(
                val countryCode: String?,
                val id: String?,
                val link: String?,
                val phone: String?,
                val type: String?
            )
        }
    }
}