package com.example.vpn_manager.modal_inbound.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpn_manager.main_screen.data.LoginRepositoryImpl
import com.example.vpn_manager.main_screen.presentation.viewmodel.MainViewModel
import com.example.vpn_manager.modal_inbound.data.AddEditInboundRepositoryImpl
import com.example.vpn_manager.modal_inbound.domain.repository.AddEditInboundRepository
import com.example.vpn_manager.modal_inbound.presentation.viewmodel.AddEditInboundViewModel
import com.example.vpn_manager.modal_server.data.AddEditServerRepositoryImpl
import com.example.vpn_manager.modal_server.domain.repository.AddEditServerRepository
import com.example.vpn_manager.storage.datasource.StorageFileDataSource
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject

class AddEditInboundViewModelFactory @Inject constructor(
    private val repository: AddEditInboundRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditInboundViewModel(repository) as T
    }
}