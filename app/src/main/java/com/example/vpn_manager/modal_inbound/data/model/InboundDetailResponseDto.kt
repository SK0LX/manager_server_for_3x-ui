package com.example.vpn_manager.modal_inbound.data.model

data class InboundDetailResponseDto(
    val success: Boolean,
    val msg: String,
    val obj: InboundDetailDto?
)

data class InboundDetailDto(
    val id: Int,
    val up: Long,
    val down: Long,
    val total: Long,
    val allTime: Long? = null,
    val remark: String,
    val enable: Boolean,
    val expiryTime: Long,
    val port: Int,
    val trafficReset: String? = null,
    val clientStats: Any? = null,
    val protocol: String,
    val settings: String,
    val streamSettings: String,
    val sniffing: String,
    val tag: String
)