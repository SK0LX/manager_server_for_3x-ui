package com.example.vpn_manager.clients_screen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpn_manager.clients_screen.data.ClientsRepositoryImpl
import com.example.vpn_manager.clients_screen.domain.model.ClientsEntity
import com.example.vpn_manager.clients_screen.domain.repository.ClientsRepository
import com.example.vpn_manager.clients_screen.presentation.model.ClientsState
import com.example.vpn_manager.clients_screen.presentation.viewmodel.ClientsEffect.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ClientsViewModel(private val repository: ClientsRepository) : ViewModel() {
    private val _state = MutableStateFlow(ClientsState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<ClientsEffect>()
    val effects = _effects.asSharedFlow()

    fun onAction(intent: ClientsIntent) {
        viewModelScope.launch {
            when (intent) {
                is ClientsIntent.DeleteClient -> deleteClient(intent.serverId, intent.inboundId, intent.client)
                ClientsIntent.LoadClients -> loadClients()
                ClientsIntent.NavigateToBack -> {
                    _effects.emit(NavigateToBack)
                }

                is ClientsIntent.NavigateToModalClient -> {
                    _effects.emit(
                        NavigateToModal(
                            serverId = state.value.serverId!!,
                            inboundId = state.value.inboundId!!,
                            clientUUID = intent.client?.uuid
                            )
                    )
                }

                is ClientsIntent.SetServerAndInboundId -> {
                    setServerAndInboundId(intent.serverId, intent.inboundId)
                }

                ClientsIntent.NavigateToModal -> {
                    _effects.emit(
                        NavigateToModal(
                            clientUUID = null,
                            inboundId = _state.value.inboundId,
                            serverId = _state.value.serverId!!
                        )
                    )
                }

                is ClientsIntent.OpenAndEditClientModal -> {

                    _effects.emit(OpenAndEditClientModal(intent.clientId))
                }

                is ClientsIntent.SetOnLongClickListener -> {
                    val serverId = state.value.serverId
                    val inboundId = state.value.inboundId
                    if (serverId.isNullOrEmpty() && inboundId.isNullOrEmpty())
                        _effects.emit(ClientsEffect.ShowError("не загрузился сервер айди или инбаунд айди"))
                    else
                        onAction(ClientsIntent.DeleteClient(serverId!!, inboundId!!, intent.client))
                    true
                }
            }
        }
    }

    private suspend fun loadClients() {
        _state.value = _state.value.copy(isLoading = true)

        try {
            if (state.value.serverId.isNullOrEmpty() && state.value.inboundId.isNullOrEmpty()){
                throw Exception("Сервер или инбаунд не найден")
            }
            val clients = repository.getClients(state.value.serverId!!, state.value.inboundId!!)
            _state.value = _state.value.copy(
                clients = clients,
                isLoading = false,
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Неизвестная ошибка"
            )
            _effects.emit(ClientsEffect.ShowError(e.message ?: "Ошибка загрузки клиентов"))
        }
    }


    private suspend fun deleteClient(serverId: String,
                                     inboundId : String,
                                     client: ClientsEntity,) {
        try {
            val success = repository.deleteClient(serverId, inboundId, client.uuid)
            if (success) {
                val updatedClients = state.value.clients.filter {
                    it.id != client.id
                }
                _state.value = _state.value.copy(
                    clients = updatedClients,
                    selectedClient = if (state.value.selectedClient?.id == client.id) {
                        null
                    } else {
                        state.value.selectedClient
                    }
                )
                _effects.emit(ClientsEffect.ShowSuccess("Клиент успешно удален"))
            } else {
                _effects.emit(ClientsEffect.ShowError("Ошибка удаления клиента"))
            }
        } catch (e: Exception) {
            _effects.emit(ClientsEffect.ShowError("Ошибка удаления: ${e.message}"))
        }
    }

    private fun setServerAndInboundId(serverId: String, inboundId: String) {
        _state.value = _state.value.copy(
            serverId = serverId,
            inboundId = inboundId
        )
    }
}