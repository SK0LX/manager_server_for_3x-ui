package com.example.vpn_manager.modal_inbound.presentation.viewmodel

interface AddEditInboundEffect {
    data class ShowMessage(val message: String) : AddEditInboundEffect
    object NavigateToInbounds : AddEditInboundEffect
}