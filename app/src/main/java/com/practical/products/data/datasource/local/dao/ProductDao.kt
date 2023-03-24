package com.practical.products.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practical.products.data.datasource.local.entity.ProductEntity

/**
 * it provides access to [ProductEntity] underlying database
 * */
@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    fun getAllProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProduct(products: List<ProductEntity>)
}
