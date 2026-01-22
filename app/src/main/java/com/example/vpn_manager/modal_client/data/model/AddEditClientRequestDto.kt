package com.example.vpn_manager.modal_client.data.model
data class UpdateClientRequestDto(
    val id: Int,
    val settings: String
)
data class ClientDetailDto(
    val id: String,
    val uuid: String,
    val inboundId: String,
    val email: String,
    val enable: Boolean,
    val totalGB: Long,
    val expiryTime: Long,
    val flow: String,
    val limitIp: Int,
    val comment: String,
    val reset: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val subId: String,
    val tgId: String,
    val up: Long = 0,
    val down: Long = 0,
    val lastOnline: Long = 0
)

data class AddClientRequestDto(
    val id: Int,
    val settings: String
)