package com.example.vpn_manager.settings_screen.di

import com.example.vpn_manager.settings_screen.data.SettingsRepositoryImpl
import com.example.vpn_manager.settings_screen.data.datasource.SettingsApi
import com.example.vpn_manager.settings_screen.data.datasource.SettingsApiStub
import com.example.vpn_manager.settings_screen.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class SettingsModal {

    @Binds
    @Singleton
    abstract fun provideSettingsRepository(impl: SettingsRepositoryImpl) : SettingsRepository

    @Binds
    @Singleton
    abstract fun provideSettingsApi(impl: SettingsApiStub): SettingsApi
}