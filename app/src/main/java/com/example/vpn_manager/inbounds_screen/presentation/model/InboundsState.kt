package com.example.vpn_manager.inbounds_screen.presentation.model

import com.example.vpn_manager.inbounds_screen.domain.model.InboundEntity
import com.example.vpn_manager.inbounds_screen.presentation.viewmodel.InboundsEffect

data class InboundsState (
    val selectedInbound: InboundEntity? = null,
    val inbounds: List<InboundEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false,
    var serverId: String? = null
)

sealed interface InboundsUiAction{
    data class SubmitClick(val username: String, val password: String) : InboundsUiAction
    data class ShowEditInboundDialog(val inboundId: String) : InboundsEffect
    object ErrorShown : InboundsUiAction
}