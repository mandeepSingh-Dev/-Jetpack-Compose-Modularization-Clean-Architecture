package com.uae.feature_home.remote.model.response


import com.google.gson.annotations.SerializedName

data class SubCategoriesListResponse(
    val count: Int?,
    val `data`: List<SubCategoryData>?,
    val message: String?,
    val status: Int?
) {
    data class SubCategoryData(
        val action: Boolean?,
        val actionItem: ActionItem?,
        val active: Int?,
        val bgColor: String?,
        val bgImgSrc: String?,
//        val category: Category?,
        @SerializedName("_id")
        val id: String?,
        val imgSrc: String?,
        val name: String?
    ) {
        data class ActionItem(
            val id : String?,
            val link : String?,
            val phone : String?,
            val type : String?
        )

        data class Category(
            @SerializedName("_id")
            val id: String?,
            val name: String?
        )
    }
}