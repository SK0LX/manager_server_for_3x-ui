package com.example.vpn_manager.settings_screen.presentation.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpn_manager.settings_screen.data.SettingsRepositoryImpl
import com.example.vpn_manager.settings_screen.domain.repository.SettingsRepository
import com.example.vpn_manager.settings_screen.presentation.modal.SettingsState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(val repository : SettingsRepository) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects = _effects.asSharedFlow()

    fun onAction(intent: SettingsIntent) {
        viewModelScope.launch {
            when (intent) {
                SettingsIntent.LoadSettings -> loadSettings()
                is SettingsIntent.ToggleTheme -> toggleTheme(intent.isDarkTheme)
                SettingsIntent.NavigateBack -> {
                    _effects.emit(SettingsEffect.NavigateBack)
                }
            }
        }
    }

    private suspend fun loadSettings() {
        _state.value = _state.value.copy(isLoading = true)

        try {
            val settings = repository.getSettings()
            _state.value = _state.value.copy(
                settingsData = settings,
                isLoading = false
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
            )
            _effects.emit(SettingsEffect.ShowError(e.message ?: "Error loading settings"))
        }
    }

    private suspend fun toggleTheme(isDarkTheme: Boolean) {
        _state.value = _state.value.copy(isLoading = true)

        try {
            repository.saveThemePreference(isDarkTheme)
            val currentSettings = _state.value.settingsData
            _state.value = _state.value.copy(
                settingsData = currentSettings?.copy(isDarkTheme = isDarkTheme),
                isLoading = false
            )
            _effects.emit(SettingsEffect.ThemeUpdated)
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false
            )
            _effects.emit(SettingsEffect.ShowError(e.message ?: "Error saving theme"))
        }
    }
}