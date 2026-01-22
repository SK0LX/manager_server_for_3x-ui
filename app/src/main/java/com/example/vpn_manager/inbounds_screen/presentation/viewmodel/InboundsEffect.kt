package com.example.vpn_manager.inbounds_screen.presentation.viewmodel

interface InboundsEffect {
    data class ShowError(val message: String) : InboundsEffect
    object NavigateToBack : InboundsEffect
    object NavigateToClients: InboundsEffect
    class NavigateToModalInbounds(var inboundId: String?, var serverId: String): InboundsEffect
    class NavigateToClientsFragment(var inboundId: String?, var serverId: String): InboundsEffect
    class AddEditInboundModal(val inboundId: String?, val serverId: String): InboundsEffect
}