package com.practical.products.data.datasource.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.practical.products.data.datasource.local.dao.ProductDao
import com.practical.products.data.datasource.local.entity.ProductEntity
import com.practical.products.utils.Constants

/**
 * To manage data items that can be accessed, updated
 * & maintain relationships between them
 */
@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun dao(): ProductDao

    companion object {
        @Volatile
        private var instance: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(appContext: Context) = Room.databaseBuilder(
            appContext,
            ProductDatabase::class.java,
            Constants.PRODUCT_DATABASE,
        ).fallbackToDestructiveMigration().build()
    }
}
