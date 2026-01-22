package com.example.vpn_manager.inbounds_screen.di

import com.example.vpn_manager.inbounds_screen.data.InboundRepositoryImpl
import com.example.vpn_manager.inbounds_screen.data.datasouce.InboundsApi
import com.example.vpn_manager.inbounds_screen.domain.repository.InboundRepository
import com.example.vpn_manager.storage.repository.StorageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class InboundsModule {

    @Binds
    @Singleton
    abstract fun provideInboundRepository(impl: InboundRepositoryImpl): InboundRepository
}