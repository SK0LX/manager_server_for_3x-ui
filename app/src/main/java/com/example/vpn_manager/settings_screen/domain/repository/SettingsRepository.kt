package com.example.vpn_manager.settings_screen.domain.repository

import com.example.vpn_manager.settings_screen.domain.modal.SettingsEntity

interface SettingsRepository {
    suspend fun getSettings(): SettingsEntity
    suspend fun saveThemePreference(isDarkTheme: Boolean)
}