package com.example.vpn_manager.settings_screen.data.datasource

interface SettingsApi {
    suspend fun getThemePreference(): Boolean
    suspend fun saveThemePreference(isDarkTheme: Boolean)
    suspend fun getAppVersion(): String
}