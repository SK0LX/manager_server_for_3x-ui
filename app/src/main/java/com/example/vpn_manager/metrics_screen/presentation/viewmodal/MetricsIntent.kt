package com.example.vpn_manager.metrics_screen.presentation.viewmodal

sealed class MetricsIntent {
    data class LoadServerMetrics(val serverId: String) : MetricsIntent()
    data class SelectServer(val serverId: String) : MetricsIntent()
    object NavigateBack: MetricsIntent()
    object AdvancedMetricsClick: MetricsIntent()
    data class SetServerId(val serverId: String?): MetricsIntent()
}