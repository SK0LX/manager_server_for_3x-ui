package com.example.vpn_manager.modal_server.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpn_manager.MainScreen.Model.main_screen.AddEditServerViewModel
import com.example.vpn_manager.modal_server.data.AddEditServerRepositoryImpl
import com.example.vpn_manager.modal_server.domain.repository.AddEditServerRepository
import com.example.vpn_manager.storage.datasource.StorageFileDataSource
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject

class AddEditServerViewModelFactory @Inject constructor(
    private val repository: AddEditServerRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return AddEditServerViewModel(repository) as T
    }
}