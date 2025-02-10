package com.secta9ine.rest.did.di

import android.app.Application
import com.secta9ine.rest.did.data.local.repository.DataStoreRepositoryImpl
import com.secta9ine.rest.did.data.local.repository.dataStore
import com.secta9ine.rest.did.domain.repository.DataStoreRepository
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
    fun provideDataStore(app: Application): DataStoreRepository =
        DataStoreRepositoryImpl(app.dataStore)
}