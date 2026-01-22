package com.example.vpn_manager.metrics_screen.presentation.viewmodal

sealed interface MetricsEffect {
    data class ShowError(val message: String) : MetricsEffect
    data class ShowMessage(val message: String) : MetricsEffect
    object NavigateBack : MetricsEffect
}