package com.example.vpn_manager.storage.repository

import com.example.vpn_manager.storage.datasource.StorageApi
import com.example.vpn_manager.storage.model.StorageEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    private val storageApi: StorageApi
) {


    suspend fun addServer(server: StorageEntity) {
        storageApi.addServer(server)
    }

    suspend fun updateServer(server: StorageEntity) {
        storageApi.updateServer(server)
    }

    suspend fun deleteServer(serverId: String) {
        storageApi.deleteServer(serverId)
    }

    suspend fun getServer(serverId: String): StorageEntity? {
        return storageApi.getServer(serverId)
    }

    suspend fun getAllServers(): List<StorageEntity> {
        return storageApi.getAllServers()
    }

    suspend fun getLastServerId(): String? {
        return storageApi.getLastServerId()
    }
}