package com.example.vpn_manager.metrics_screen.di

import com.example.vpn_manager.metrics_screen.data.MetricsRepositoryImpl
import com.example.vpn_manager.metrics_screen.domain.repository.MetricsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class MetricsModule {

    @Binds
    @Singleton
    abstract fun provideMetricsRepository(impl: MetricsRepositoryImpl): MetricsRepository
}