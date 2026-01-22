package com.example.vpn_manager.main_screen.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpn_manager.main_screen.data.LoginRepositoryImpl
import com.example.vpn_manager.main_screen.domain.repository.MainRepository
import com.example.vpn_manager.main_screen.presentation.viewmodel.MainViewModel
import com.example.vpn_manager.storage.datasource.StorageFileDataSource
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject
import javax.inject.Singleton

class MainViewModelFactory @Inject constructor(
    val mainRepository : MainRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(mainRepository) as T
    }
}