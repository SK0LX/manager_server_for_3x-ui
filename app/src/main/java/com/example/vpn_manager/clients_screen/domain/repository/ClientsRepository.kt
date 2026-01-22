package com.example.vpn_manager.clients_screen.domain.repository

import com.example.vpn_manager.clients_screen.domain.model.ClientsEntity

interface ClientsRepository {
    suspend fun getClients(serverId: String, inboundId: String): List<ClientsEntity>
    suspend fun deleteClient(serverId: String,
                             inboundId: String,
                             clientId: String): Boolean
}