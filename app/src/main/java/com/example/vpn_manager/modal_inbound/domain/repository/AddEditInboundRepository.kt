package com.example.vpn_manager.modal_inbound.domain.repository

import com.example.vpn_manager.modal_inbound.domain.model.AddEditInboundEntity

interface AddEditInboundRepository {
    suspend fun getInbound(servinboundId: Int?, serverId: String): AddEditInboundEntity
    suspend fun addInbound(inbound: AddEditInboundEntity, serverId: String): AddEditInboundEntity
    suspend fun updateInbound(inbound: AddEditInboundEntity, serverId: String): AddEditInboundEntity
}