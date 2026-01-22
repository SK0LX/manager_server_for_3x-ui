package com.example.vpn_manager.settings_screen.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpn_manager.settings_screen.data.SettingsRepositoryImpl
import com.example.vpn_manager.settings_screen.data.datasource.SettingsApiStub
import com.example.vpn_manager.settings_screen.domain.repository.SettingsRepository
import com.example.vpn_manager.settings_screen.presentation.viewmodal.SettingsViewModel
import javax.inject.Inject

class SettingsViewModelFactory @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            settingsRepository
        ) as T
    }
}