package com.example.vpn_manager.metrics_screen.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vpn_manager.metrics_screen.domain.repository.MetricsRepository
import com.example.vpn_manager.metrics_screen.presentation.viewmodal.MetricsViewModel
import javax.inject.Inject

class MetricsViewModelFactory @Inject constructor(private val metricsRepository: MetricsRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MetricsViewModel(metricsRepository) as T
    }
}