package com.example.vpn_manager.settings_screen.presentation.viewmodal

sealed interface SettingsEffect {
    data class ShowError(val message: String) : SettingsEffect
    object ThemeUpdated : SettingsEffect
    object NavigateBack: SettingsEffect
}