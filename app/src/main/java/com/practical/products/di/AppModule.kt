package com.practical.products.di

import android.content.Context
import com.google.gson.Gson
import com.practical.products.data.datasource.local.LocalDataSource
import com.practical.products.data.datasource.local.dao.ProductDao
import com.practical.products.data.datasource.remote.ApiServices
import com.practical.products.data.datasource.remote.RemoteDataSourceImpl
import com.practical.products.data.repository.ProductRepositoryImpl
import com.practical.products.domain.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * provide different types of data with[dagger.hilt]
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    fun provideApiServices(retrofitClient: Retrofit): ApiServices {
        return retrofitClient.create(ApiServices::class.java)
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        api: ApiServices,
        localDataSource: LocalDataSource,
        @ApplicationContext appContext: Context,
    ): ProductRepository {
        val remoteDataSourceImpl = RemoteDataSourceImpl(api)
        return ProductRepositoryImpl(
            remoteDataSourceImpl,
            localDataSource = localDataSource,
            appContext,
        )
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(productDao: ProductDao): LocalDataSource {
        return LocalDataSource(productDao)
    }
}
