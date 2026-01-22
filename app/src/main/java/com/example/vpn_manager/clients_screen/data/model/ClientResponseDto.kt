package com.example.vpn_manager.clients_screen.data.model


data class ClientsResponseDto(
    val success: Boolean,
    val msg: String,
    val obj: List<InboundWithClientsDto?>
)

data class InboundWithClientsDto(
    val id: Int,
    val clientStats: List<ClientResponseDto>?,
    val up: Long?,
    val down: Long?,
    val total: Long?,
    val remark: String?,
    val enable: Boolean?,
    val expiryTime: Long?,
    val port: Int?,
    val protocol: String?,
    val tag: String?
)
data class ClientResponseDto(
    val id: Int,
    val inboundId: Int,
    val enable: Boolean,
    val email: String,
    val uuid: String,
    val subId: String,
    val up: Long,
    val down: Long,
    val allTime: Long,
    val expiryTime: Long,
    val total: Long,
    val reset: Long,
    val lastOnline: Long
)