package com.example.vpn_manager.settings_screen.presentation.viewmodal


sealed class SettingsIntent {
    object LoadSettings : SettingsIntent()
    data class ToggleTheme(val isDarkTheme: Boolean) : SettingsIntent()
    object NavigateBack: SettingsIntent()
}