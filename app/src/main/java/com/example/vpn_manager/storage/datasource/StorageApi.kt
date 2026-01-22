package com.example.vpn_manager.storage.datasource

import com.example.vpn_manager.storage.model.StorageEntity

interface StorageApi {
    suspend fun addServer(server: StorageEntity)
    suspend fun updateServer(server: StorageEntity)
    suspend fun deleteServer(serverId: String)
    suspend fun getServer(serverId: String): StorageEntity?
    suspend fun getAllServers(): List<StorageEntity>
    suspend fun getLastServerId(): String?
}