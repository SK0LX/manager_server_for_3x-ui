package com.example.vpn_manager.storage.datasource

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.vpn_manager.storage.model.StorageEntity
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageFileDataSource @Inject constructor(
    private val context: Context
) : StorageApi {

    private companion object {
        private const val SERVERS_FILE_NAME = "servers.json"
        private const val PREFS_NAME = "vpn_storage"
        private const val LAST_ID_KEY = "last_server_id"
    }


    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .create()

    private val serversFile = File(context.filesDir, SERVERS_FILE_NAME)
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private data class ServerStorage(
        val servers: Map<String, StorageEntity> = emptyMap()
    )

    init {
        if (!serversFile.exists()) {
            serversFile.parentFile?.mkdirs()
            val initialData = ServerStorage()
            serversFile.writeText(gson.toJson(initialData))
        }
    }

    private suspend fun readServers(): ServerStorage = withContext(Dispatchers.IO) {
        return@withContext try {
            if (serversFile.exists()) {
                val jsonString = serversFile.readText()
                if (jsonString.isNotEmpty()) {
                    gson.fromJson(jsonString, ServerStorage::class.java)
                } else {
                    ServerStorage()
                }
            } else {
                ServerStorage()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ServerStorage()
        }
    }

    private suspend fun writeServers(serverStorage: ServerStorage) = withContext(Dispatchers.IO) {
        try {
            val jsonString = gson.toJson(serverStorage)
            serversFile.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to save servers: ${e.message}")
        }
    }

    override suspend fun addServer(server: StorageEntity) {
        val serverStorage = readServers()
        val newServers = serverStorage.servers.toMutableMap()
        val serverId = if (server.id.isEmpty()) generateServerId() else server.id
        newServers[serverId] = server.copy(id = serverId)
        saveLastServerId(serverId)

        writeServers(ServerStorage(newServers))
    }

    override suspend fun updateServer(server: StorageEntity) {
        val serverStorage = readServers()
        val newServers = serverStorage.servers.toMutableMap()

        if (newServers.containsKey(server.id)) {
            newServers[server.id] = server
            writeServers(ServerStorage(newServers))
        } else {
            throw Exception("Server with id ${server.id} not found")
        }
    }

    override suspend fun deleteServer(serverId: String) {
        val serverStorage = readServers()
        val newServers = serverStorage.servers.toMutableMap()

        if (newServers.remove(serverId) != null) {
            writeServers(ServerStorage(newServers))
        } else {
            throw Exception("Server with id $serverId not found")
        }
    }

    override suspend fun getServer(serverId: String): StorageEntity? {
        val serverStorage = readServers()
        return serverStorage.servers[serverId]
    }

    override suspend fun getAllServers(): List<StorageEntity> {
        val serverStorage = readServers()
        return serverStorage.servers.values.toList()
    }

    override suspend fun getLastServerId(): String? {
        return prefs.getString(LAST_ID_KEY, null)
    }

    private fun generateServerId(): String {
        return UUID.randomUUID().toString()
    }

    private fun saveLastServerId(id: String) {
        prefs.edit().putString(LAST_ID_KEY, id).apply()
    }
}