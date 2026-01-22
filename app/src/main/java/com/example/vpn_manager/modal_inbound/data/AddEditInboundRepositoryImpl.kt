package com.example.vpn_manager.modal_inbound.data

import com.example.vpn_manager.modal_inbound.data.datasource.AddEditApi
import com.example.vpn_manager.modal_inbound.data.mapper.toEntity
import com.example.vpn_manager.modal_inbound.data.mapper.toRequestDto
import com.example.vpn_manager.modal_inbound.domain.model.AddEditInboundEntity
import com.example.vpn_manager.modal_inbound.domain.repository.AddEditInboundRepository
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddEditInboundRepositoryImpl @Inject constructor(
    private val api: AddEditApi,
    private val storageRepository: StorageRepository
) : AddEditInboundRepository {

    override suspend fun getInbound(servinboundId: Int?, serverId: String): AddEditInboundEntity {
        val storageEntity = requireNotNull(storageRepository.getServer(serverId))
        val fullUrl = if (storageEntity.url.endsWith("/")) {
            "${storageEntity.url}panel/api/inbounds/get/${servinboundId}"
        } else {
            "${storageEntity.url}/panel/api/inbounds/get/${servinboundId}"
        }
        val response = api.getInbound(fullUrl, storageEntity.cookies)
        if (!response.success || response.obj == null) {
            throw Exception(response.msg)
        }
        return response.obj.toEntity()
    }

    override suspend fun addInbound(inbound: AddEditInboundEntity, serverId: String): AddEditInboundEntity {
        val storageEntity = requireNotNull(storageRepository.getServer(serverId))
        val response = api.addInbound(storageEntity.url + "panel/api/inbounds/add", storageEntity.cookies,  inbound.toRequestDto())
        if (!response.success || response.obj == null) {
            throw Exception(response.msg)
        }
        return response.obj.toEntity()
    }

    override suspend fun updateInbound(inbound: AddEditInboundEntity, serverId: String): AddEditInboundEntity {
        val storageEntity = requireNotNull(storageRepository.getServer(serverId))
        val fullUrl = if (storageEntity.url.endsWith("/")) {
            "${storageEntity.url}panel/api/inbounds/update/${inbound.id}"
        } else {
            "${storageEntity.url}/panel/api/inbounds/update/${inbound.id}"
        }
        val response = api.updateInbound(fullUrl, storageEntity.cookies,inbound.toRequestDto())
        if (!response.success || response.obj == null) {
            throw Exception(response.msg)
        }
        return response.obj.toEntity()
    }
}