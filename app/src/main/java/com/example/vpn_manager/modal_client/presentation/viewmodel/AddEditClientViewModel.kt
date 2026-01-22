package com.example.vpn_manager.modal_client.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpn_manager.modal_client.domain.model.AddEditClientEntity
import com.example.vpn_manager.modal_client.domain.repository.AddEditClientRepository
import com.example.vpn_manager.modal_client.presentation.model.AddEditClientState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditClientViewModel(
    private val repository: AddEditClientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditClientState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<AddEditClientEffect>()
    val effects = _effects.asSharedFlow()

    fun setClientData(serverId: String?, inboundId: String?, clientUUID: String?) {
        _state.value = _state.value.copy(
            serverId = serverId,
            inboundId = inboundId,
            uuid = clientUUID,
            isEditMode = clientUUID != null,
        )

        if (clientUUID != null && serverId != null && inboundId != null) {
            loadClient(serverId, inboundId, clientUUID)
        }
    }

    fun onAction(intent: AddEditClientIntent) {
        when (intent) {
            is AddEditClientIntent.AddClient -> addClient(intent)
            is AddEditClientIntent.UpdateClient -> updateClient(intent)
            is AddEditClientIntent.SetClientData -> setClientData(intent.serverId, intent.inboundId, intent.clientUUID)
            is AddEditClientIntent.NavigateToClients -> {
                viewModelScope.launch {
                    _effects.emit(AddEditClientEffect.NavigateToClients)
                }
            }
        }
    }

    private fun addClient(intent: AddEditClientIntent.AddClient) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val serverId = state.value.serverId ?: throw IllegalStateException("Server ID не найден")

                val client = AddEditClientEntity(
                    id = null,
                    inboundId = intent.inboundId,
                    email = intent.email,
                    enable = intent.enable,
                    totalGB = intent.totalGB,
                    expiryTime = intent.expiryTime,
                )
                val success = repository.addClient(serverId, client)

                if (success) {
                    _state.value = _state.value.copy(isLoading = false)
                    _effects.emit(AddEditClientEffect.NavigateToClients)
                    _effects.emit(AddEditClientEffect.ShowMessage("Клиент успешно добавлен"))
                } else {
                    throw Exception("Не удалось добавить клиента")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка при добавлении"
                )
                _effects.emit(AddEditClientEffect.ShowMessage("Ошибка добавления: ${e.message}"))
            }
        }
    }

    private fun updateClient(intent: AddEditClientIntent.UpdateClient) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val serverId = state.value.serverId ?: throw IllegalStateException("Server ID не найден")
                val clientUUID = state.value.uuid ?: throw IllegalStateException("Client UUID не найден")
                val inboundId = state.value.inboundId ?: throw IllegalStateException("Inbound ID не найден")

                val client = AddEditClientEntity(
                    id = clientUUID,
                    inboundId = inboundId,
                    email = intent.email,
                    enable = intent.enable,
                    totalGB = intent.totalGB,
                    expiryTime = intent.expiryTime,
                )
                repository.updateClient(serverId, client, clientUUID)

                _state.value = _state.value.copy(isLoading = false)
                _effects.emit(AddEditClientEffect.NavigateToClients)
                _effects.emit(AddEditClientEffect.ShowMessage("Клиент успешно обновлен"))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка при обновлении"
                )
                _effects.emit(AddEditClientEffect.ShowMessage("Ошибка обновления: ${e.message}"))
            }
        }
    }

    private fun loadClient(serverId: String, inboundId: String, clientUUID: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val client = repository.getClient(serverId, clientUUID, inboundId)
                if (client != null) {
                    _state.value = _state.value.copy(
                        id = client.id,
                        inboundId = client.inboundId,
                        email = client.email,
                        enable = client.enable,
                        totalGB = client.totalGB.toString(),
                        expiryTime = client.expiryTime.toString(),
                        isLoading = false
                    )
                } else {
                    throw Exception("Клиент не найден")
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Не удалось загрузить данные"
                )
                _effects.emit(AddEditClientEffect.ShowMessage("Ошибка загрузки: ${e.message}"))
            }
        }
    }
}