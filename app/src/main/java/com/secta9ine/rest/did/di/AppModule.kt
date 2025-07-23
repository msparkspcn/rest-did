package com.secta9ine.rest.did.di

import android.app.Application
import android.content.res.Resources
import com.secta9ine.rest.did.data.local.database.AppDatabase
import com.secta9ine.rest.did.data.local.repository.DataStoreRepositoryImpl
import com.secta9ine.rest.did.data.local.repository.DeviceRepositoryImpl
import com.secta9ine.rest.did.data.local.repository.OrderStatusRepositoryImpl
import com.secta9ine.rest.did.data.local.repository.ProductRepositoryImpl
import com.secta9ine.rest.did.data.local.repository.SaleOpenRepositoryImpl
import com.secta9ine.rest.did.data.local.repository.dataStore
import com.secta9ine.rest.did.data.remote.api.AuthInterceptor
import com.secta9ine.rest.did.data.remote.api.RestApiService
import com.secta9ine.rest.did.data.remote.repository.RestApiRepositoryImpl
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
import com.secta9ine.rest.did.domain.repository.DeviceRepository
import com.secta9ine.rest.did.domain.repository.OrderStatusRepository
import com.secta9ine.rest.did.domain.repository.ProductRepository
import com.secta9ine.rest.did.domain.repository.RestApiRepository
import com.secta9ine.rest.did.domain.repository.SaleOpenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Provides
    @Singleton
    fun provideResources(app: Application): Resources = app.resources
    @Provides
    @Singleton
    fun provideDataStore(app: Application): DataStoreRepository =
        DataStoreRepositoryImpl(app.dataStore)

    @Singleton
    @Provides
    fun provideAuthInterceptor(): AuthInterceptor = AuthInterceptor()

    @Singleton
    @Provides
    fun provideRestApiService(): RestApiService = RestApiService.create()

    @Singleton
    @Provides
    fun provideRestApiRepository(restApiService: RestApiService, app: Application): RestApiRepository =
        RestApiRepositoryImpl(app.resources, restApiService)
    @Singleton
    @Provides
    fun provideAppDatabase(app: Application): AppDatabase = AppDatabase.create(app)

    @Singleton
    @Provides
    fun provideDeviceRepository(db: AppDatabase): DeviceRepository =
        DeviceRepositoryImpl(db.deviceDao)

    @Singleton
    @Provides
    fun provideOrderStatusRepository(db: AppDatabase): OrderStatusRepository =
        OrderStatusRepositoryImpl(db.orderStatusDao)

    @Singleton
    @Provides
    fun provideProductRepository(db: AppDatabase): ProductRepository =
        ProductRepositoryImpl(db.productDao)

    @Singleton
    @Provides
    fun provideSaleOpenRepository(db: AppDatabase): SaleOpenRepository =
        SaleOpenRepositoryImpl(db.saleOpenDao)
}