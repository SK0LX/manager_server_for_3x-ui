package com.example.vpn_manager.modal_client.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpn_manager.modal_client.presentation.viewmodel.AddEditClientViewModel
import com.example.vpn_manager.modal_client.data.AddEditClientRepositoryImpl
import com.example.vpn_manager.modal_client.domain.repository.AddEditClientRepository
import com.example.vpn_manager.modal_inbound.domain.repository.AddEditInboundRepository
import com.example.vpn_manager.storage.datasource.StorageFileDataSource
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddEditClientViewModelFactory @Inject constructor(
    private val repository: AddEditClientRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditClientViewModel(repository) as T
    }
}