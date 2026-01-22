package com.example.vpn_manager.modal_inbound.presentation.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpn_manager.modal_inbound.domain.model.AddEditInboundEntity
import com.example.vpn_manager.modal_inbound.domain.repository.AddEditInboundRepository
import com.example.vpn_manager.modal_inbound.presentation.model.AddEditInboundState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditInboundViewModel(
    private val repository: AddEditInboundRepository) : ViewModel() {

    private val _state = MutableStateFlow(AddEditInboundState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<AddEditInboundEffect>()
    val effects = _effects.asSharedFlow()

    fun onAction(intent: AddEditInboundIntent) {
        when (intent) {
            is AddEditInboundIntent.AddInbound -> AddInboundEntity(intent)
            is AddEditInboundIntent.UpdateInbound -> UpdateInboundEntity(intent)
            is AddEditInboundIntent.GetInboundId -> GetInboundIdEntity()
            is AddEditInboundIntent.SetInboundAndServerId -> setInboundAndServerId(intent.inboundId, intent.serverId)
            is AddEditInboundIntent.LoadInbound -> loadInbound(intent.inboundId)
            AddEditInboundIntent.NavigateToInbounds -> {
                viewModelScope.launch {
                    _effects.emit(AddEditInboundEffect.NavigateToInbounds)
                }
            }
        }
    }

    private fun GetInboundIdEntity() {
        viewModelScope.launch {
            repository.getInbound(state.value.inboundId, state.value.serverId.toString())
        }
    }

    private fun UpdateInboundEntity(intent: AddEditInboundIntent.UpdateInbound) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val id = state.value.inboundId ?: throw IllegalStateException("ID не найден для обновления")
                val inbound = AddEditInboundEntity(
                    id = id,
                    remark = intent.remark,
                    enable = intent.enable,
                    port = intent.port,
                    protocol = intent.protocol
                )
                val savedInbound = repository.updateInbound(inbound, state.value.serverId.toString())

                _state.value = _state.value.copy(
                    isLoading = false,
                    id = savedInbound.id
                )
                _effects.emit(AddEditInboundEffect.NavigateToInbounds)
                _effects.emit(AddEditInboundEffect.ShowMessage("Инбаунд успешно обновлен"))

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка при обновлении"
                )
                _effects.emit(AddEditInboundEffect.ShowMessage("Ошибка обновления: ${e.message}"))
            }
        }
    }

    private fun AddInboundEntity(intent: AddEditInboundIntent.AddInbound) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val inbound = AddEditInboundEntity(
                    id = null,
                    remark = intent.remark,
                    enable = intent.enable,
                    port = intent.port,
                    protocol = intent.protocol
                )
                val savedInbound = repository.addInbound(inbound, state.value.serverId.toString())

                _state.value = _state.value.copy(
                    isLoading = false,
                    id = savedInbound.id
                )
                _effects.emit(AddEditInboundEffect.NavigateToInbounds)
                _effects.emit(AddEditInboundEffect.ShowMessage("Инбаунд успешно добавлен"))

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Ошибка при добавлении"
                )
                _effects.emit(AddEditInboundEffect.ShowMessage("Ошибка добавления: ${e.message}"))
            }
        }
    }

    private fun loadInbound(inboundId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            try {
                val inbound = repository.getInbound(inboundId, state.value.serverId.toString())
                _state.value = _state.value.copy(
                    id = inbound.id,
                    remark = inbound.remark,
                    enable = inbound.enable,
                    port = inbound.port,
                    protocol = inbound.protocol,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Не удалось загрузить данные"
                )
                _effects.emit(AddEditInboundEffect.ShowMessage("Ошибка загрузки: ${e.message}"))
            }
        }
    }

    private fun setInboundAndServerId(inboundId: Int?, serverId: String?) {
        this.state.value.inboundId = inboundId
        this.state.value.serverId = serverId
        _state.value = _state.value.copy(
            isEditMode = inboundId != null
        )
        if (inboundId != null) {
            onAction(AddEditInboundIntent.LoadInbound(inboundId))
        }
    }
}
