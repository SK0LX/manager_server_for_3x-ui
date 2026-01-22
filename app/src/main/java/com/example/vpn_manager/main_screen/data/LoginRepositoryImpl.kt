package com.example.vpn_manager.main_screen.data

import com.example.vpn_manager.main_screen.data.mapper.toServerEntity
import com.example.vpn_manager.main_screen.domain.model.ServerEntity
import com.example.vpn_manager.main_screen.domain.repository.MainRepository
import com.example.vpn_manager.storage.repository.StorageRepository
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(private val storageRepository: StorageRepository) : MainRepository {
    override suspend fun getAllServers(): List<ServerEntity> {
        val storageServers = storageRepository.getAllServers()
        return storageServers.toServerEntity()
    }
    override suspend fun deleteServer(serverId: String) {
        storageRepository.deleteServer(serverId)
    }

}