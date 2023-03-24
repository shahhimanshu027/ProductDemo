package com.practical.products.domain.repository

import com.practical.products.data.common.HandleResult
import com.practical.products.data.response.ProductData
import kotlinx.coroutines.flow.Flow

/**
 * To make an interaction between
 * [com.practical.products.domain.usecase.GetProductUseCase] &
 * [com.practical.products.data.repository.ProductRepositoryImpl]
 * */
interface ProductRepository {
    fun getProduct(): Flow<HandleResult<List<ProductData>>>
}
