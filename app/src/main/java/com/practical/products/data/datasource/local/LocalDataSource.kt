package com.practical.products.data.datasource.local

import com.practical.products.data.datasource.local.dao.ProductDao
import com.practical.products.data.datasource.local.entity.ProductEntity

/**
 * it is used for do different type of database operation with help of [ProductDao]
 */
class LocalDataSource constructor(private val productDao: ProductDao) {

    fun getAllProducts() = productDao.getAllProducts().asReversed()
    suspend fun insertProducts(products: List<ProductEntity>) = productDao.insertAllProduct(products)
}
