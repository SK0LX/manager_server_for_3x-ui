package com.example.vpn_manager.settings_screen.presentation.modal

import com.example.vpn_manager.settings_screen.domain.modal.SettingsEntity

data class SettingsState(
    val isLoading: Boolean = false,
    val settingsData: SettingsEntity? = null
)