// MainViewModel.kt
package com.example.vpn_manager.main_screen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpn_manager.main_screen.domain.repository.MainRepository
import com.example.vpn_manager.main_screen.presentation.model.MainUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MainRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainUiState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<MainEffect>()
    val effects = _effects.asSharedFlow()

    init {
        loadServers()
    }

    fun onAction(intent: MainIntent) {
        when (intent) {
            is MainIntent.LoadServers -> loadServers()
            is MainIntent.DeleteServer -> deleteServer(intent.serverId)
            is MainIntent.ClearError -> clearError()
            is MainIntent.RefreshServers -> refreshServers()
            is MainIntent.NavigateToAddEditServer -> {
                viewModelScope.launch {
                    _effects.emit(MainEffect.NavigateAddOrUpdateIServers(intent.serverId))
                }
            }

            MainIntent.NavigateToSettings -> {
                viewModelScope.launch {
                    _effects.emit(MainEffect.NavigateToSettings)
                }
            }

            is MainIntent.NavigateToServerMetrics -> {
                viewModelScope.launch {
                    if (intent.serverId != null)
                        _effects.emit(MainEffect.NavigateToServerMetrics(intent.serverId))
                    else
                        _effects.emit(MainEffect.ShowMessage("No server selected"))
                }
            }

            is MainIntent.NavigateToInbounds -> {
                viewModelScope.launch {
                    _effects.emit(MainEffect.NavigateToInbounds(intent.serverId))
                }
            }
        }
    }

    private fun loadServers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                val servers = repository.getAllServers()
                val serversMap = servers.associateBy { it.id.hashCode() }

                _state.value = _state.value.copy(
                    servers = serversMap,
                    isLoading = false,
                    error = null
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load servers: ${e.message}"
                )
                _effects.emit(MainEffect.ShowMessage("Error loading servers: ${e.message}"))
            }
        }
    }

    private fun deleteServer(serverId: String) {
        viewModelScope.launch {
            try {
                repository.deleteServer(serverId)
                val servers = repository.getAllServers()
                val serversMap = servers.associateBy { it.id.hashCode() }

                _state.value = _state.value.copy(
                    servers = serversMap,
                    selectedServer = state.value.selectedServer?.takeIf { it.id != serverId }
                )

                _effects.emit(MainEffect.ShowMessage("Server deleted"))

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete server: ${e.message}"
                )
                _effects.emit(MainEffect.ShowMessage("Error deleting server: ${e.message}"))
            }
        }
    }

    private fun refreshServers() {
        loadServers()
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}