package com.example.vpn_manager.metrics_screen.presentation.modal

import com.example.vpn_manager.metrics_screen.domain.modal.MetricsEntity


data class MetricsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val metricsData: MetricsEntity? = null,
    val selectedServerId: String = "overall",
    val serverId: String? = null
)

sealed interface MetricsUiAction {
    data class SubmitClick(val username: String, val password: String) : MetricsUiAction
    object ErrorShown : MetricsUiAction
}