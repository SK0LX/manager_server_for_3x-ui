package com.example.vpn_manager.clients_screen.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpn_manager.storage.datasource.StorageFileDataSource
import com.example.vpn_manager.storage.repository.StorageRepository
import com.example.vpn_manager.clients_screen.data.ClientsRepositoryImpl
import com.example.vpn_manager.clients_screen.domain.repository.ClientsRepository
import com.example.vpn_manager.clients_screen.presentation.viewmodel.ClientsViewModel
import javax.inject.Inject

class ClientsViewModelFactory @Inject constructor(
    private val clientsRepository: ClientsRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClientsViewModel(clientsRepository) as T
    }
}