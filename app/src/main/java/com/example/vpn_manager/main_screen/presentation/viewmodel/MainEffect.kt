package com.example.vpn_manager.main_screen.presentation.viewmodel

interface MainEffect {
    object NavigateToSettings : MainEffect
    data class ShowMessage(val message: String) : MainEffect
    data class NavigateToServerMetrics(val serverId: String?) : MainEffect
    data class NavigateAddOrUpdateIServers(val serverId: String?) : MainEffect
    data class NavigateToInbounds(val serverId: String): MainEffect
}