package com.example.vpn_manager.MainScreen.Model.main_screen

import com.example.vpn_manager.modal_inbound.presentation.viewmodel.AddEditInboundIntent

sealed class AddEditServerIntent {
    data class AddServer(
        val name: String,
        val ip: String,
        val username: String,
        val password: String
    ) : AddEditServerIntent()

    data class UpdateServer(
        val name: String,
        val ip: String,
        val username: String,
        val password: String
    ) : AddEditServerIntent()

    object GetServerId : AddEditServerIntent()

    data class SetServerId(val serverId: String?): AddEditServerIntent()
    data class LoadServer(val serverId: String): AddEditServerIntent()
    object NavigateBack : AddEditServerIntent()
}