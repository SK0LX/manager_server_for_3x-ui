package com.example.vpn_manager.clients_screen.domain.model

data class ClientsEntity(
    val id: String,
    val uuid: String,
    val isWork: Boolean,
    val name: String,
    val isOnline: String,
    val traffic: List<String>,
    val dateEnd: String,
    val subscribeLink: String?
)