package com.example.vpn_manager.modal_client.domain.repository

import com.example.vpn_manager.modal_client.domain.model.AddEditClientEntity

interface AddEditClientRepository {
    suspend fun getClient(serverId: String, clientId: String, inboundId: String): AddEditClientEntity?
    suspend fun addClient(serverId: String, client: AddEditClientEntity): Boolean
    suspend fun updateClient(serverId: String, client: AddEditClientEntity, clientUUID: String): AddEditClientEntity
}