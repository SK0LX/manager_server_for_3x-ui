package com.example.vpn_manager.inbounds_screen.domain.repository

import com.example.vpn_manager.inbounds_screen.domain.model.InboundEntity

interface InboundRepository {
    suspend fun getInbounds(serverId: String): List<InboundEntity>
    suspend fun deleteInbounds(serverId: String, inboundId : String): Boolean
}
