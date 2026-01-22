package com.example.vpn_manager.clients_screen.presentation.viewmodel

import com.example.vpn_manager.clients_screen.domain.model.ClientsEntity

sealed class ClientsIntent {
    data class DeleteClient(
        val serverId: String,
        val inboundId : String,
        val client: ClientsEntity,
    ) : ClientsIntent()
    object LoadClients : ClientsIntent()
    object NavigateToBack: ClientsIntent()
    data class SetServerAndInboundId(
        val serverId: String,
        val inboundId: String
    ): ClientsIntent()

    object NavigateToModal: ClientsIntent()
    data class NavigateToModalClient(
        val client: ClientsEntity? = null
    ) : ClientsIntent()

    data class OpenAndEditClientModal(val clientId: String?): ClientsIntent()
    data class SetOnLongClickListener(val client: ClientsEntity): ClientsIntent()
}