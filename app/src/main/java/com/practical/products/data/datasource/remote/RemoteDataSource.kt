package com.practical.products.data.datasource.remote

import com.practical.products.data.common.HandleResult
import com.practical.products.data.response.ProductListResponse

/**
 * interface between
 * [com.practical.products.data.repository.ProductRepositoryImpl] and [RemoteDataSourceImpl]
 */
interface RemoteDataSource {
    suspend fun getProductList(): HandleResult<ProductListResponse>
}
