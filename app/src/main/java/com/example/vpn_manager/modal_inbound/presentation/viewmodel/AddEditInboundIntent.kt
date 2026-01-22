package com.example.vpn_manager.modal_inbound.presentation.viewmodel

import com.example.vpn_manager.modal_inbound.domain.model.AddEditInboundEntity

sealed class AddEditInboundIntent {
    data class AddInbound(
        val remark: String,
        val enable: Boolean,
        val port: String,
        val protocol: String
    ) : AddEditInboundIntent()

    data class UpdateInbound(
        val remark: String,
        val enable: Boolean,
        val port: String,
        val protocol: String
    ) : AddEditInboundIntent()

    object GetInboundId : AddEditInboundIntent()
    data class SetInboundAndServerId(val inboundId: Int?,val serverId: String?) : AddEditInboundIntent()
    data class LoadInbound(val inboundId: Int) : AddEditInboundIntent()
    object NavigateToInbounds : AddEditInboundIntent()
}