package com.example.vpn_manager.inbounds_screen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vpn_manager.inbounds_screen.data.InboundRepositoryImpl
import com.example.vpn_manager.inbounds_screen.domain.repository.InboundRepository
import com.example.vpn_manager.inbounds_screen.presentation.model.InboundsState
import com.example.vpn_manager.inbounds_screen.presentation.viewmodel.InboundsEffect.*
import com.example.vpn_manager.inbounds_screen.presentation.viewmodel.InboundsIntent.*
import com.example.vpn_manager.main_screen.presentation.viewmodel.MainEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InboundsViewModel(private val repository: InboundRepository) : ViewModel() {

    private val _state = MutableStateFlow(InboundsState())
    val state = _state.asStateFlow()
    private val _effects = MutableSharedFlow<InboundsEffect>()
    val effects = _effects.asSharedFlow()

    fun onAction(intent: InboundsIntent) {
        viewModelScope.launch {
            when (intent) {
                is InboundsIntent.LoadInbounds -> loadInbounds()
                is InboundsIntent.DeleteInbound -> deleteInbound(intent.inboundId)
                is InboundsIntent.SetServerId -> setServerId(intent.serverId)
                is InboundsIntent.OnModalGo -> {
                    val serverId = state.value.serverId
                    if (serverId != null) {
                        _effects.emit(NavigateToModalInbounds(null, serverId))
                    } else {
                        _effects.emit(ShowError("Server ID not found"))
                    }
                }

                is InboundsIntent.NavigateToBack -> {
                    _effects.emit(InboundsEffect.NavigateToBack)
                }

                is InboundsIntent.OnEditClicked -> {
                    state.value.serverId?.let{
                        onAction(OpenAddEditInboundModal(intent.inboundId, it))
                    } ?: run{
                        _effects.emit(ShowError("Server ID not found"))
                    }
                }

                is InboundsIntent.OpenAddEditInboundModal -> {
                    _effects.emit(InboundsEffect.AddEditInboundModal(intent.inboundId, intent.serverId))
                }
                is InboundsIntent.NavigateToClientsFragment -> {
                    state.value.serverId?.let {
                        _effects.emit(InboundsEffect.NavigateToClientsFragment(intent.inboundId, it))
                    } ?: run {
                        _effects.emit(InboundsEffect.ShowError("Server ID not found"))
                    }
                }
            }
        }
    }

    private suspend fun deleteInbound(id: String) {
        val serverId = state.value.serverId
        if (serverId.isNullOrEmpty())
            throw Exception("ServerId is null or empty")
        else{
            repository.deleteInbounds(serverId, id)
            loadInbounds()
        }
    }

    private fun loadInbounds(){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            try {
                val serverId = state.value.serverId
                if (serverId.isNullOrEmpty())
                    throw Exception("ServerId is null or empty")
                val inbounds = repository.getInbounds(serverId)
                _state.value = _state.value.copy(
                    inbounds = inbounds,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "Неизвестная ошибка"
                )
                _effects.emit(ShowError(e.message ?: "Ошибка загрузки"))
            }
        }

    }

    private fun setServerId(serverId: String?) {
        _state.value = _state.value.copy(serverId = serverId)
        viewModelScope.launch {
            loadInbounds()
        }
    }
}