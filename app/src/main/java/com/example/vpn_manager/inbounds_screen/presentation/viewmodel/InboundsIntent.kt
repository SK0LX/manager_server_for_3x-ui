package com.example.vpn_manager.inbounds_screen.presentation.viewmodel

import com.example.vpn_manager.inbounds_screen.domain.model.InboundEntity

sealed class InboundsIntent {
    object LoadInbounds : InboundsIntent()
    class DeleteInbound(val inboundId: String) : InboundsIntent()
    class SetServerId(val serverId: String?) : InboundsIntent()
    class OnModalGo : InboundsIntent()
    object NavigateToBack : InboundsIntent()
    class OnEditClicked(val inboundId: String?, val serverId: String?): InboundsIntent()
    class OpenAddEditInboundModal(val inboundId: String?, val serverId: String): InboundsIntent()
    class NavigateToClientsFragment(val inboundId: String?, val serverId: String?): InboundsIntent()
}