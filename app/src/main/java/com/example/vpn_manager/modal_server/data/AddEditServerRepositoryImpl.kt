package com.example.vpn_manager.modal_server.data

import com.example.vpn_manager.modal_server.data.datasource.AddEditServerApi
import com.example.vpn_manager.modal_server.data.mapper.toAddEditServerEntity
import com.example.vpn_manager.modal_server.data.mapper.toCookies
import com.example.vpn_manager.modal_server.data.model.AddEditServerRequestDto
import com.example.vpn_manager.modal_server.domain.model.LoginFailedException
import com.example.vpn_manager.modal_server.domain.model.AddEditServerEntity
import com.example.vpn_manager.modal_server.domain.repository.AddEditServerRepository
import com.example.vpn_manager.storage.model.StorageEntity
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject

class AddEditServerRepositoryImpl @Inject constructor(
    private val api: AddEditServerApi,
    private val storageRepository: StorageRepository
) : AddEditServerRepository {

    override suspend fun getServer(serverId: String?): AddEditServerEntity {
        if (serverId == null) {
            throw Exception("Server ID is null")
        }
        return requireNotNull(storageRepository.getServer(serverId)).toAddEditServerEntity()
    }

    override suspend fun addServer(server: AddEditServerEntity): AddEditServerEntity {
        return try {
            val loginEntity = loginServer(
                name = server.name,
                username = server.username,
                password = server.password,
                url = server.url
            )
            val serverWithCookies = server.copy(
                cookies = loginEntity.cookies ?: "",
                id = null
            )

            saveServerToStorage(serverWithCookies, hasValidCookies = true)

        } catch (loginException: Exception) {
            val serverWithoutCookies = server.copy(
                cookies = "",
                id = null
            )

            saveServerToStorage(serverWithoutCookies, hasValidCookies = false)
            throw LoginFailedException(
                message = "Login failed: ${loginException.message}",
                server = saveServerToStorage(serverWithoutCookies, false),
                cause = loginException
            )
        }
    }

    override suspend fun updateServer(server: AddEditServerEntity): AddEditServerEntity {
        require(server.id != null) { "Server ID must not be null for update" }

        return try {
            val loginEntity = loginServer(
                name = server.name,
                username = server.username,
                password = server.password,
                url = server.url
            )

            val serverWithCookies = server.copy(cookies = loginEntity.cookies ?: "")

            updateServerInStorage(serverWithCookies, hasValidCookies = true)

        } catch (loginException: Exception) {
            val oldServer = storageRepository.getServer(server.id!!)
            val oldCookies = oldServer?.cookies ?: ""
            val serverWithOldCookies = server.copy(cookies = oldCookies)

            updateServerInStorage(serverWithOldCookies, hasValidCookies = oldCookies.isNotEmpty())
            throw LoginFailedException(
                message = "Login failed: ${loginException.message}",
                server = serverWithOldCookies,
                cause = loginException,
                hasOldCookies = oldCookies.isNotEmpty()
            )
        }
    }

    override suspend fun loginServer(
        name: String,
        username: String,
        password: String,
        url: String
    ): AddEditServerEntity {
        val response = api.login(AddEditServerRequestDto(name, username, password, url + "login/"), url + "login/")
        if (!response.isSuccessful) {
            throw Exception("Login failed: ${response.code()} - ${response.errorBody()?.string()}")
        }

        val cookies = response.toCookies()

        if (cookies.cookies.isBlank()) {
            throw Exception("No cookies received from server")
        }

        return AddEditServerEntity(
            id = null,
            name = name,
            url = url,
            username = username,
            password = password,
            cookies = cookies.cookies
        )
    }

    private suspend fun saveServerToStorage(
        server: AddEditServerEntity,
        hasValidCookies: Boolean
    ): AddEditServerEntity {
        val storageEntity = StorageEntity(
            id = "",
            name = server.name,
            url = server.url,
            username = server.username,
            password = server.password,
            cookies = server.cookies ?: ""
        )

        storageRepository.addServer(storageEntity)

        val lastServerId = storageRepository.getLastServerId()
        val savedStorageEntity = if (lastServerId != null) {
            storageRepository.getServer(lastServerId)
        } else {
            throw Exception("Failed to get saved server ID")
        }

        return savedStorageEntity?.let {
            AddEditServerEntity(
                id = it.id,
                name = it.name,
                url = it.url,
                username = it.username,
                password = it.password,
                cookies = it.cookies
            )
        } ?: throw Exception("Failed to retrieve saved server")
    }

    private suspend fun updateServerInStorage(
        server: AddEditServerEntity,
        hasValidCookies: Boolean
    ): AddEditServerEntity {
        val storageEntity = StorageEntity(
            id = server.id!!,
            name = server.name,
            url = server.url,
            username = server.username,
            password = server.password,
            cookies = server.cookies ?: ""
        )

        storageRepository.updateServer(storageEntity)

        return server
    }
}
