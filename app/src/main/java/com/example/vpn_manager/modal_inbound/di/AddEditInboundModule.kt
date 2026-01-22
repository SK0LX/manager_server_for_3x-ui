package com.example.vpn_manager.modal_inbound.di

import com.example.vpn_manager.modal_inbound.data.AddEditInboundRepositoryImpl
import com.example.vpn_manager.modal_inbound.data.datasource.AddEditApi
import com.example.vpn_manager.modal_inbound.domain.repository.AddEditInboundRepository
import com.example.vpn_manager.modal_server.data.datasource.AddEditServerApi
import com.example.vpn_manager.storage.repository.StorageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AddEditInboundModule {

    @Binds
    @Singleton
    abstract fun bindAddEditInboundRepository(impl: AddEditInboundRepositoryImpl) : AddEditInboundRepository
}