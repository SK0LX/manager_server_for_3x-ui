package com.example.vpn_manager.modal_server.data.model


data class AddEditServerRequestDto(
    val name: String,
    val username: String,
    val password: String,
    val url: String
)