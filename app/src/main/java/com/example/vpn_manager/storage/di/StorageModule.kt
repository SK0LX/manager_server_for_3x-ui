package com.example.vpn_manager.storage.di

import com.example.vpn_manager.storage.datasource.StorageApi
import com.example.vpn_manager.storage.datasource.StorageFileDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun provideStorageApi(
        storageFileDataSource: StorageFileDataSource
    ): StorageApi {
        return storageFileDataSource
    }

}