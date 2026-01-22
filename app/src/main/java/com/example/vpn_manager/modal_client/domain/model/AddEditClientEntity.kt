package com.example.vpn_manager.modal_client.domain.model

data class AddEditClientEntity(
    val id: String? = null,
    val inboundId: String,
    val email: String,
    val enable: Boolean,
    val totalGB: Long,
    val expiryTime: Long,
)