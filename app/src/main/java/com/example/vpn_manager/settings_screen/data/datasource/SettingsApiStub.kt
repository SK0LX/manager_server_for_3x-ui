package com.example.vpn_manager.settings_screen.data.datasource

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsApiStub @Inject constructor() : SettingsApi {
    private var isDarkTheme = false
    private val appVersion = "1.0.0"

    override suspend fun getThemePreference(): Boolean = isDarkTheme

    override suspend fun saveThemePreference(isDarkTheme: Boolean) {
        this.isDarkTheme = isDarkTheme
    }

    override suspend fun getAppVersion(): String = appVersion
}