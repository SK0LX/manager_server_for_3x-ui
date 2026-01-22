package com.example.vpn_manager.modal_client.presentation.viewmodel

sealed class AddEditClientIntent {
    data class AddClient(
        val inboundId: String,
        val email: String,
        val enable: Boolean,
        val totalGB: Long,
        val expiryTime: Long,
    ) : AddEditClientIntent()

    data class UpdateClient(
        val email: String,
        val enable: Boolean,
        val totalGB: Long,
        val expiryTime: Long,
    ) : AddEditClientIntent()

    data class SetClientData(
        val serverId: String?,
        val inboundId: String?,
        val clientUUID: String?
    ): AddEditClientIntent()

    object NavigateToClients: AddEditClientIntent()
}