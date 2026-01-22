package com.example.vpn_manager.clients_screen.presentation.viewmodel

sealed interface ClientsEffect {
    data class ShowError(val message: String) : ClientsEffect
    data class ShowSuccess(val message: String) : ClientsEffect
    object NavigateToBack : ClientsEffect
    data class NavigateToModal(
        val clientUUID: String?,
        val inboundId: String?,
        val serverId: String
    ): ClientsEffect

    data class OpenAndEditClientModal(val clientId: String?): ClientsEffect
}