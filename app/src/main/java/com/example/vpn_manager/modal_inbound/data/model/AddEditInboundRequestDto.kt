package com.example.vpn_manager.modal_inbound.data.model

data class AddEditInboundRequestDto(
    val remark: String,
    val enable: Boolean,
    val expiryTime: Long,
    val listen: String,
    val port: Int,
    val protocol: String,
    val settings: String,
    val streamSettings: String,
    val sniffing: String,
    val allocate: String? = null,
    val up: Long = 0,
    val down: Long = 0,
    val total: Long = 0
)