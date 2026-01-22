package com.example.vpn_manager.modal_server.domain.model

class LoginFailedException(
    message: String,
    val server: AddEditServerEntity,
    cause: Throwable? = null,
    val hasOldCookies: Boolean = false
) : Exception(message, cause)