package com.example.vpn_manager.modal_server.presentation.model

data class AddEditServerState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val id: String? = null,
    val name: String = "",
    val url: String = "",
    val username: String = "",
    val password: String = "",

    val isEditMode: Boolean = false,
    var serverId: String? = null
)