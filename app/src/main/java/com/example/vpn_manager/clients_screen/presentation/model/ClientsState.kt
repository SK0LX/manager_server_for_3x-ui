package com.example.vpn_manager.clients_screen.presentation.model

import com.example.vpn_manager.clients_screen.domain.model.ClientsEntity


data class ClientsState(
    val selectedClient: ClientsEntity? = null,
    val clients: List<ClientsEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val serverId: String? = null,
    val inboundId: String? = null
)

sealed interface ClientsUiAction{
    data class SubmitClick(val username: String, val password: String) : ClientsUiAction
    object ErrorShown : ClientsUiAction
}