package com.practical.products.di

import android.content.Context
import com.practical.products.data.datasource.local.dao.ProductDao
import com.practical.products.data.datasource.local.db.ProductDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * provide different types of data with[dagger.hilt]
 */
@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): ProductDatabase =
        ProductDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideProductsDao(db: ProductDatabase): ProductDao = db.dao()
}
