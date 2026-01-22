package com.example.vpn_manager.storage.model


data class StorageEntity(
    val id: String,
    val name: String,
    val url: String,
    val username: String,
    val password: String,
    val cookies: String = ""
)
