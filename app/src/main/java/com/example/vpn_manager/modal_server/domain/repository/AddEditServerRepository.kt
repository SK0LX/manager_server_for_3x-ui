package com.example.vpn_manager.modal_server.domain.repository

import com.example.vpn_manager.main_screen.domain.model.ServerEntity
import com.example.vpn_manager.modal_server.domain.model.AddEditServerEntity

interface AddEditServerRepository {
    suspend fun getServer(serverId: String?): AddEditServerEntity
    suspend fun addServer(server: AddEditServerEntity): AddEditServerEntity
    suspend fun updateServer(server: AddEditServerEntity): AddEditServerEntity

    suspend fun loginServer(name:String, username: String, password: String, url : String): AddEditServerEntity
}