package com.practical.products.data.datasource.remote

import com.practical.products.data.response.ProductListResponse
import com.practical.products.utils.EndPoints.GET_PRODUCT_LIST
import retrofit2.Response
import retrofit2.http.GET

/**
 * interface for all API calls
 */
interface ApiServices {
    @GET(GET_PRODUCT_LIST)
    suspend fun getProductList(): Response<ProductListResponse>
}
