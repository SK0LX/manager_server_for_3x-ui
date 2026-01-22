package com.example.vpn_manager.main_screen.domain.repository

import com.example.vpn_manager.main_screen.domain.model.ServerEntity

interface MainRepository{
    suspend fun getAllServers(): List<ServerEntity>
    suspend fun deleteServer(serverId: String)
}