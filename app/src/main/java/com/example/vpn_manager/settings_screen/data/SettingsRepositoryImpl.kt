package com.example.vpn_manager.settings_screen.data

import com.example.vpn_manager.settings_screen.data.datasource.SettingsApi
import com.example.vpn_manager.settings_screen.data.datasource.SettingsApiStub
import com.example.vpn_manager.settings_screen.data.mapper.toEntity
import com.example.vpn_manager.settings_screen.data.model.SettingsDataDto
import com.example.vpn_manager.settings_screen.domain.repository.SettingsRepository
import com.example.vpn_manager.settings_screen.domain.modal.SettingsEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(var settingsApi: SettingsApi) : SettingsRepository {

    override suspend fun getSettings(): SettingsEntity {
        val isDarkTheme = settingsApi.getThemePreference()
        val appVersion = settingsApi.getAppVersion()
        val dto = SettingsDataDto(isDarkTheme, appVersion)
        return dto.toEntity()
    }

    override suspend fun saveThemePreference(isDarkTheme: Boolean) {
        settingsApi.saveThemePreference(isDarkTheme)
    }
}