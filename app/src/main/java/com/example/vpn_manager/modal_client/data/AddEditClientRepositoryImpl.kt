package com.example.vpn_manager.modal_client.data

import com.example.vpn_manager.modal_client.data.datasource.AddEditClientApi
import com.example.vpn_manager.modal_client.data.mapper.toClientDetailDto
import com.example.vpn_manager.modal_client.data.mapper.toEntity
import com.example.vpn_manager.modal_client.data.mapper.toSettingsJson
import com.example.vpn_manager.modal_client.data.mapper.toUpdateRequestDto
import com.example.vpn_manager.modal_client.data.model.AddClientRequestDto
import com.example.vpn_manager.modal_client.data.model.ClientDetailDto
import com.example.vpn_manager.modal_client.domain.model.AddEditClientEntity
import com.example.vpn_manager.modal_client.domain.repository.AddEditClientRepository
import com.example.vpn_manager.storage.repository.StorageRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddEditClientRepositoryImpl @Inject constructor(
    private val api: AddEditClientApi,
    private val storageRepository: StorageRepository
) : AddEditClientRepository {

    override suspend fun getClient(serverId: String, clientId: String, inboundId: String): AddEditClientEntity? {
        val server = requireNotNull(storageRepository.getServer(serverId))
        val response = api.getInbounds(server.url + "panel/api/inbounds/list", server.cookies).toClientDetailDto(inboundId, clientId)
        if (response == null)
            return null
        return response.toEntity()
    }

    override suspend fun addClient(serverId: String, client: AddEditClientEntity): Boolean  {
        val server = requireNotNull(storageRepository.getServer(serverId))

        try {
            val inboundIdInt = client.inboundId.toIntOrNull() ?: throw IllegalArgumentException("Invalid inboundId")
            val settingsJson = client.toSettingsJson()
            val fullUrl = if (server.url.endsWith("/")) {
                "${server.url}panel/api/inbounds/addClient"
            } else {
                "$server.url/panel/api/inbounds/addClient"
            }
            val requestBody = AddClientRequestDto(
                id = inboundIdInt,
                settings = settingsJson
            )
            return api.addClient(fullUrl,  requestBody, server.cookies).success
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    override suspend fun updateClient(serverId: String, client: AddEditClientEntity, clientUUID: String): AddEditClientEntity {
        require(client.id != null) { "Client ID must not be null for update" }

        val server = requireNotNull(storageRepository.getServer(serverId))
        val fullUrl = if (server.url.endsWith("/")) {
            "${server.url}panel/api/inbounds/updateClient/${clientUUID}"
        } else {
            "$server.url/panel/api/inbounds/updateClient${clientUUID}"
        }

        try {
            val request = client.toUpdateRequestDto()
            val response = api.updateClient(fullUrl, request, server.cookies)

            if (!response.success) {
                throw Exception(response.msg)
            }
            return client
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}