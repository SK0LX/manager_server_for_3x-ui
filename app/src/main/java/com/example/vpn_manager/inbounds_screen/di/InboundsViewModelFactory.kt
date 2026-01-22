package com.example.vpn_manager.inbounds_screen.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpn_manager.inbounds_screen.data.InboundRepositoryImpl
import com.example.vpn_manager.inbounds_screen.domain.repository.InboundRepository
import com.example.vpn_manager.inbounds_screen.presentation.viewmodel.InboundsViewModel
import com.example.vpn_manager.storage.datasource.StorageFileDataSource
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject

class InboundsViewModelFactory @Inject constructor(
    private val repository: InboundRepository
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InboundsViewModel(repository) as T
    }
}