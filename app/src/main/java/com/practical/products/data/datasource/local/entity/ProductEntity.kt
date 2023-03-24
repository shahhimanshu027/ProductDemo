package com.practical.products.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * database table
 */
@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "discountPercentage") var discountPercentage: Float,
    @ColumnInfo(name = "rating") var rating: Float,
    @ColumnInfo(name = "stock") var stock: Int,
    @ColumnInfo(name = "brand") var brand: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "thumbnail") var thumbnail: String,
    @ColumnInfo(name = "images") var images: String,
)
