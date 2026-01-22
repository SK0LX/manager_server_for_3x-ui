package com.example.vpn_manager.metrics_screen.presentation.viewmodal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpn_manager.metrics_screen.domain.repository.MetricsRepository
import com.example.vpn_manager.metrics_screen.presentation.modal.MetricsState
import com.example.vpn_manager.metrics_screen.presentation.viewmodal.MetricsEffect.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MetricsViewModel(private val repository: MetricsRepository) : ViewModel() {

    private val _state = MutableStateFlow(MetricsState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<MetricsEffect>()
    val effects = _effects.asSharedFlow()

    fun onAction(intent: MetricsIntent) {
        viewModelScope.launch {
            when (intent) {
                is MetricsIntent.LoadServerMetrics -> loadServerMetrics(intent.serverId)
                is MetricsIntent.SelectServer -> selectServer(intent.serverId)
                MetricsIntent.NavigateBack -> {
                    _effects.emit(MetricsEffect.NavigateBack)
                }

                MetricsIntent.AdvancedMetricsClick -> {
                    _effects.emit(ShowMessage("Advanced metrics coming soon"))
                }

                is MetricsIntent.SetServerId -> {
                    setServerId(intent.serverId)
                }
            }
        }
    }

    private suspend fun loadServerMetrics(serverId: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)

        try {
            val metrics = repository.getServerMetrics(serverId)
            _state.value = _state.value.copy(
                metricsData = metrics,
                isLoading = false,
                selectedServerId = serverId
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Failed to load server metrics"
            )
            _effects.emit(MetricsEffect.ShowError(e.message ?: "Error loading server metrics"))
        }
    }

    private suspend fun setServerId(serverId: String?) {
        if (!serverId.isNullOrEmpty()) {
            _state.value = _state.value.copy(
                serverId = serverId,
                selectedServerId = serverId
            )
            loadServerMetrics(serverId)
        } else {
            _effects.emit(MetricsEffect.ShowError("ServerId is null or empty"))
        }
    }

    private fun selectServer(serverId: String) {
        _state.value = _state.value.copy(selectedServerId = serverId)
    }
}