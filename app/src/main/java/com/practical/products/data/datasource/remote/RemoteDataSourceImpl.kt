package com.practical.products.data.datasource.remote

import com.practical.products.R
import com.practical.products.data.common.DataSourceException
import com.practical.products.data.common.HandleResult
import com.practical.products.data.common.RequestErrorHandler
import com.practical.products.data.response.ProductListResponse

/**
 * This repository is responsible for
 * calling API and check it's success and failure state
 */
class RemoteDataSourceImpl(private val apiServices: ApiServices) : RemoteDataSource {
    override suspend fun getProductList(): HandleResult<ProductListResponse> {
        return try {
            val result = apiServices.getProductList()
            if (result.isSuccessful) {
                result.body()?.let {
                    HandleResult.Success(it)
                } ?: run {
                    HandleResult.Error(
                        DataSourceException.Unexpected(R.string.error_unexpected_message),
                    )
                }
            } else {
                HandleResult.Error(DataSourceException.Server(R.string.error_server_unexpected_message))
            }
        } catch (e: Exception) {
            HandleResult.Error(RequestErrorHandler.getRequestError(e))
        }
    }
}
