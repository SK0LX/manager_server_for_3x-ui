package com.example.vpn_manager.main_screen.presentation.model

import com.example.vpn_manager.main_screen.domain.model.ServerEntity

data class MainUiState   (
    val selectedServer: ServerEntity? = null,
    val servers: Map<Int, ServerEntity> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginSuccess: Boolean = false,
)

sealed interface MainUiAction{
    data class SubmitClick(val username: String, val password: String) : MainUiAction
    object ErrorShown : MainUiAction
}

