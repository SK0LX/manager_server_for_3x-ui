package com.example.vpn_manager.main_screen.data.mapper

import com.example.vpn_manager.main_screen.domain.model.ServerEntity
import com.example.vpn_manager.storage.model.StorageEntity


fun List<StorageEntity>.toServerEntity() : List<ServerEntity> {
    return this.map { storageEntity ->
        ServerEntity(
            id = storageEntity.id,
            name = storageEntity.name,
            url = storageEntity.url,
            login = storageEntity.username,
            password = storageEntity.password,
            cookies = storageEntity.cookies,
            isWork = false
        )
    }
}