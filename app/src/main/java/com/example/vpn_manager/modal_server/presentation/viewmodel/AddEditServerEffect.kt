package com.example.vpn_manager.MainScreen.Model.main_screen

sealed interface AddEditServerEffect {
    data class ShowMessage(val message: String) : AddEditServerEffect
    object NavigateBack : AddEditServerEffect
}