package com.example.vpn_manager.modal_client.presentation.model

data class AddEditClientState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val id: String? = null,
    val uuid: String? = null,
    val inboundId: String? = null,
    val serverId: String? = null,

    val email: String = "",
    val enable: Boolean = true,
    val totalGB: String = "1000",
    val expiryTime: String = "",
    val limitIp: String = "2",

    val isEditMode: Boolean = false
)