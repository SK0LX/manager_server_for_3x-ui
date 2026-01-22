package com.example.vpn_manager.inbounds_screen.data

import com.example.vpn_manager.inbounds.data.mapper.toInboundListEntity
import com.example.vpn_manager.inbounds_screen.data.datasouce.InboundsApi
import com.example.vpn_manager.inbounds_screen.domain.model.InboundEntity
import com.example.vpn_manager.inbounds_screen.domain.repository.InboundRepository
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InboundRepositoryImpl @Inject constructor(
    private val loginApi: InboundsApi,
    private val storageRepository: StorageRepository
) : InboundRepository {
    override suspend fun getInbounds(serverId: String): List<InboundEntity> {
        val server = requireNotNull(storageRepository.getServer(serverId))
        return loginApi.getInbounds(server.url + "panel/api/inbounds/list", server.cookies).toInboundListEntity()
    }


    override suspend fun deleteInbounds(serverId: String, inboundId : String): Boolean {
        val server = requireNotNull(storageRepository.getServer(serverId))
        return loginApi.deleteInbound(server.url + "panel/api/inbounds/del/${inboundId}", server.cookies).success
    }
}