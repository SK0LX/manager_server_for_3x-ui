package com.example.vpn_manager.clients_screen.di

import com.example.vpn_manager.clients_screen.data.ClientsRepositoryImpl
import com.example.vpn_manager.clients_screen.data.datasource.ClientsApi
import com.example.vpn_manager.clients_screen.domain.repository.ClientsRepository
import com.example.vpn_manager.storage.repository.StorageRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ClientsModule {

    @Singleton
    @Binds
    abstract fun provideClientsRepository(impl: ClientsRepositoryImpl): ClientsRepository
}