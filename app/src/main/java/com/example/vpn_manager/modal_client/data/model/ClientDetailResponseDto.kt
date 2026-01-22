package com.example.vpn_manager.modal_client.data.model

data class ClientDetailResponseDto(
    val success: Boolean,
    val msg: String,
    val obj: Any? = null
)

data class InboundResponse(
    val success: Boolean,
    val msg: String,
    val obj: List<InboundDto>? = null
)

data class InboundDto(
    val id: Int,
    val remark: String?,
    val enable: Boolean,
    val port: Int,
    val protocol: String,
    val settings: String,
    val streamSettings: String,
    val sniffing: String,
    val clientStats: List<ClientStatDto>? = null,
    val up: Long = 0,
    val down: Long = 0,
    val total: Long = 0
)

data class ClientStatDto(
    val id: Int? = null,
    val inboundId: Int? = null,
    val enable: Boolean,
    val email: String,
    val uuid: String,
    val subId: String,
    val up: Long = 0,
    val down: Long = 0,
    val allTime: Long = 0,
    val expiryTime: Long = 0,
    val total: Long = 0,
    val reset: Long = 0,
    val lastOnline: Long = 0
)


data class AddClientResponseDto(
    val success: Boolean,
    val msg: String,
    val obj: Any? = null
)