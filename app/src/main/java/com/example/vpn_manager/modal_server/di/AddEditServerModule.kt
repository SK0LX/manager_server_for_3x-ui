package com.example.vpn_manager.modal_server.di

import com.example.vpn_manager.modal_server.data.AddEditServerRepositoryImpl
import com.example.vpn_manager.modal_server.data.datasource.AddEditServerApi
import com.example.vpn_manager.modal_server.domain.repository.AddEditServerRepository
import com.example.vpn_manager.storage.repository.StorageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AddEditServerModule {

    @Binds
    @Singleton
    abstract fun provideAddEditModalRepository(impl: AddEditServerRepositoryImpl) : AddEditServerRepository
}