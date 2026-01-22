package com.example.vpn_manager.clients_screen.data

import com.example.vpn_manager.clients_screen.data.datasource.ClientsApi
import com.example.vpn_manager.storage.repository.StorageRepository
import com.example.vpn_manager.clients_screen.data.mapper.toListClientsEntity
import com.example.vpn_manager.clients_screen.domain.model.ClientsEntity
import com.example.vpn_manager.clients_screen.domain.repository.ClientsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientsRepositoryImpl @Inject constructor(
    private val clientsApi: ClientsApi,
    private val storageRepository: StorageRepository
) : ClientsRepository {
    override suspend fun getClients(serverId: String, inboundId: String): List<ClientsEntity> {
        val server = requireNotNull(storageRepository.getServer(serverId))
        val response = clientsApi.getInboundsWithClients(server.url + "panel/api/inbounds/list", server.cookies)
        return response.toListClientsEntity(inboundId)
    }

    override suspend fun deleteClient(serverId: String,
                                      inboundId: String,
                                      clientUUId: String): Boolean {
        val server = requireNotNull(storageRepository.getServer(serverId))
        return clientsApi.deleteClient(
            url = server.url + "panel/api/inbounds/${inboundId}/delClient/${clientUUId}",
            cookies = server.cookies
        ).success
    }
}