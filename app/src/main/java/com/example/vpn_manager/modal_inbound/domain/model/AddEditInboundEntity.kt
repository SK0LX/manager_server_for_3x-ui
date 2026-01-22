package com.example.vpn_manager.modal_inbound.domain.model

data class AddEditInboundEntity(
    val id: Int? = null,

    // Основные поля
    val remark: String,
    val enable: Boolean,
    val protocol: String,
    val port: String,
)