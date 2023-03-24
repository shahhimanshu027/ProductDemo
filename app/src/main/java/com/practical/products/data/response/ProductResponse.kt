package com.practical.products.data.response

import com.google.gson.annotations.SerializedName

/**
 * Created by Shah on 23-03-2023.
 */

data class ProductListResponse(
    var products: List<ProductData>,
    var total: Int,
    var skip: Int,
    var limit: Int,
)

data class ProductData(
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("description") var description: String,
    @SerializedName("price") var price: Int,
    @SerializedName("discountPercentage") var discountPercentage: Float,
    @SerializedName("rating") var rating: Float,
    @SerializedName("stock") var stock: Int,
    @SerializedName("brand") var brand: String,
    @SerializedName("category") var category: String,
    @SerializedName("thumbnail") var thumbnail: String,
    @SerializedName("images") var images: List<String>,
)


