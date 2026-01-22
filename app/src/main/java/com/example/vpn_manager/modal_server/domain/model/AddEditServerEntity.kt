package com.example.vpn_manager.modal_server.domain.model

data class AddEditServerEntity(
    val id: String? = null,
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val cookies: String? = null
)