package com.example.vpn_manager.main_screen.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerEntity(
    val id: String,
    var url: String,
    val name: String,
    val login: String,
    val password: String,
    val isWork: Boolean,
    var cookies: String
)

@Serializable
data class ServersStorage(
    val servers: MutableList<ServerEntity> = mutableListOf()
)
