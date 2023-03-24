package com.practical.products.data.response

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practical.products.data.datasource.local.entity.ProductEntity

/**
 * this class used for change datatype to
 * [ProductData] -> [ProductEntity] AND
 * [ProductEntity] -> [ProductData] For insert and data fetching purpose
 */
fun ProductData.mapToDomain() = ProductData(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    stock = stock,
    brand = brand,
    category = category,
    thumbnail = thumbnail,
    images = images,
)

fun ProductData.mapToEntity() = ProductEntity(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    stock = stock,
    brand = brand,
    category = category,
    thumbnail = thumbnail,
    images = Gson().toJson(images),
)

fun ProductEntity.mapToDomain(): ProductData = ProductData(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    stock = stock,
    brand = brand,
    category = category,
    thumbnail = thumbnail,
    images = Gson().fromJson(
        images,
        object : TypeToken<List<String?>?>() {}.type,

    ),
)
