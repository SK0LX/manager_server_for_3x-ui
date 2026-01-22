package com.example.vpn_manager.modal_client.di

import androidx.annotation.Size
import com.example.vpn_manager.modal_client.data.AddEditClientRepositoryImpl
import com.example.vpn_manager.modal_client.data.datasource.AddEditClientApi
import com.example.vpn_manager.modal_client.domain.repository.AddEditClientRepository
import com.example.vpn_manager.modal_inbound.data.AddEditInboundRepositoryImpl
import com.example.vpn_manager.storage.repository.StorageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AddEditClientModule {
    @Binds
    @Singleton
    abstract fun provideAddEditClientRepository(impl: AddEditClientRepositoryImpl): AddEditClientRepository
}