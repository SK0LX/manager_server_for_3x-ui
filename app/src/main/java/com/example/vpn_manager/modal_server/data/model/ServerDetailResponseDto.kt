package com.example.vpn_manager.modal_server.data.model

data class ServerDetailResponseDto(
    val success: Boolean,
    val msg: String,
    val obj: ServerDetailDto?
)

data class ServerDetailDto(
    val id: String,
    val name: String,
    val url: String,
    val username: String,
    val password: String
)
data class LoginResponseCookies(
    val cookies: String
)