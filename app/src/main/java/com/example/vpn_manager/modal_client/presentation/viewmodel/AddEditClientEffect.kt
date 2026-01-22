package com.example.vpn_manager.modal_client.presentation.viewmodel

interface AddEditClientEffect {
    data class ShowMessage(val message: String) : AddEditClientEffect
    object NavigateToClients : AddEditClientEffect
}