package com.example.vpn_manager.settings_screen.data.mapper

import com.example.vpn_manager.settings_screen.data.model.SettingsDataDto
import com.example.vpn_manager.settings_screen.domain.modal.SettingsEntity

fun SettingsDataDto.toEntity(): SettingsEntity {
    return SettingsEntity(
        isDarkTheme = this.isDarkTheme,
        appVersion = this.appVersion
    )
}

fun SettingsEntity.toDto(): SettingsDataDto {
    return SettingsDataDto(
        isDarkTheme = this.isDarkTheme,
        appVersion = this.appVersion
    )
}