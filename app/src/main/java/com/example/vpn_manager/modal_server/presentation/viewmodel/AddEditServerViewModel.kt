package com.example.vpn_manager.MainScreen.Model.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpn_manager.modal_server.domain.model.LoginFailedException
import com.example.vpn_manager.modal_server.domain.model.AddEditServerEntity
import com.example.vpn_manager.modal_server.domain.repository.AddEditServerRepository
import com.example.vpn_manager.modal_server.presentation.model.AddEditServerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditServerViewModel(
    private val repository: AddEditServerRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditServerState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<AddEditServerEffect>()
    val effects = _effects.asSharedFlow()


    fun onAction(intent: AddEditServerIntent) {
        when (intent) {
            is AddEditServerIntent.AddServer -> addServer(intent)
            is AddEditServerIntent.UpdateServer -> updateServer(intent)
            AddEditServerIntent.GetServerId -> getServerId()
            is AddEditServerIntent.SetServerId -> setServerId(intent.serverId)
            is AddEditServerIntent.LoadServer -> loadServer(intent.serverId)
            AddEditServerIntent.NavigateBack -> {
                viewModelScope.launch {
                    _effects.emit(AddEditServerEffect.NavigateBack)
                }
            }
        }
    }

    private fun getServerId() {
        viewModelScope.launch {
            val serverId = state.value.serverId
            if (serverId != null) {
                repository.getServer(serverId)
            }
        }
    }

    private fun addServer(intent: AddEditServerIntent.AddServer) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val server = AddEditServerEntity(
                    id = null,
                    name = intent.name,
                    url = intent.ip,
                    username = intent.username,
                    password = intent.password
                )
                val savedServer = repository.addServer(server)

                _state.value = _state.value.copy(
                    isLoading = false,
                    id = savedServer.id
                )
                _effects.emit(AddEditServerEffect.NavigateBack)
                _effects.emit(AddEditServerEffect.ShowMessage("Server added successfully"))

            } catch (e: LoginFailedException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    id = e.server.id,
                    error = "Server saved but login failed: ${e.message}"
                )

                _effects.emit(AddEditServerEffect.ShowMessage(
                    message = e.message ?: "Login failed",
                ))
                print( e.message ?: "Login failed" )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error adding server"
                )
                _effects.emit(AddEditServerEffect.ShowMessage("Error adding server: ${e.message}"))
                print("Error adding server: ${e.message}")
            }
        }
    }

    private fun updateServer(intent: AddEditServerIntent.UpdateServer) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val id = state.value.serverId ?: throw IllegalStateException("Server ID not found for update")
                val server = AddEditServerEntity(
                    id = id,
                    name = intent.name,
                    url = intent.ip,
                    username = intent.username,
                    password = intent.password
                )
                val savedServer = repository.updateServer(server)

                _state.value = _state.value.copy(
                    isLoading = false,
                    id = savedServer.id
                )
                _effects.emit(AddEditServerEffect.NavigateBack)
                _effects.emit(AddEditServerEffect.ShowMessage("Server updated successfully"))

            } catch (e: LoginFailedException) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    id = e.server.id,
                    error = if (e.hasOldCookies) {
                        "Server updated but login failed. Using old cookies."
                    } else {
                        "Server updated but login failed."
                    }
                )

                val message = if (e.hasOldCookies) {
                    "Login failed but server updated with old cookies"
                } else {
                    "Login failed but server updated"
                }

                _effects.emit(AddEditServerEffect.ShowMessage(
                    message = message,
                ))

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error updating server"
                )
                _effects.emit(AddEditServerEffect.ShowMessage("Error updating server: ${e.message}"))
            }
        }
    }

    private fun loadServer(serverId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val server = repository.getServer(serverId)
                _state.value = _state.value.copy(
                    id = server.id,
                    name = server.name,
                    url = server.url,
                    username = server.username,
                    password = server.password,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load server data"
                )
                _effects.emit(AddEditServerEffect.ShowMessage("Error loading server: ${e.message}"))
            }
        }
    }
    private fun setServerId(serverId: String?) {
        _state.value = _state.value.copy(
            serverId = serverId,
            isEditMode = serverId != null
        )
        if (serverId != null) {
            onAction(AddEditServerIntent.LoadServer(serverId))
        }
    }
}