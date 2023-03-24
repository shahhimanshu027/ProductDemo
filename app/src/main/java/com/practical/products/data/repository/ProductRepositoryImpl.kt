package com.practical.products.data.repository

import android.content.Context
import com.practical.products.data.common.HandleResult
import com.practical.products.data.common.onFlowStarts
import com.practical.products.data.datasource.local.LocalDataSource
import com.practical.products.data.datasource.remote.RemoteDataSource
import com.practical.products.data.response.ProductData
import com.practical.products.data.response.mapToDomain
import com.practical.products.data.response.mapToEntity
import com.practical.products.domain.repository.ProductRepository
import com.practical.products.utils.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * This repository is responsible for
 * fetching data[ProductData] from server or db
 * */
class ProductRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val context: Context,
) : ProductRepository {
    override fun getProduct(): Flow<HandleResult<List<ProductData>>> {
        return flow {
            if (!isInternetAvailable(context)) {
                localDataSource.getAllProducts().run {
                    emit(HandleResult.Success(map { it.mapToDomain() }.reversed()))
                }
            } else {
                remoteDataSource.getProductList().run {
                    when (this) {
                        is HandleResult.Success -> {
                            localDataSource.insertProducts(
                                response.products.map {
                                    it.mapToEntity()
                                },
                            )
                            emit(
                                HandleResult.Success(
                                    response.products.map {
                                        it.mapToDomain()
                                    },
                                ),
                            )
                        }
                        is HandleResult.Error -> {
                            localDataSource.getAllProducts().run {
                                if (this.isNotEmpty()) {
                                    emit(HandleResult.Success(map { it.mapToDomain() }))
                                } else {
                                    emit(HandleResult.Error(exception))
                                }
                            }
                        }
                        else -> {
                            // No implementation needed
                        }
                    }
                }
            }
        }.flowOn(Dispatchers.IO).onFlowStarts()
    }
}
