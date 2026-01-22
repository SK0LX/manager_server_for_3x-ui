package com.example.vpn_manager.main_screen.presentation.viewmodel

sealed class MainIntent {
    object LoadServers : MainIntent()
    data class DeleteServer(val serverId: String) : MainIntent()
    object ClearError : MainIntent()
    object RefreshServers : MainIntent()
    data class NavigateToAddEditServer(val serverId: String?) : MainIntent()
    object NavigateToSettings : MainIntent()
    data class NavigateToServerMetrics(val serverId: String?): MainIntent()
    data class NavigateToInbounds(val serverId: String): MainIntent()
}