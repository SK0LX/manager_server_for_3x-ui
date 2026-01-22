package com.example.vpn_manager.metrics_screen.domain.repository

import com.example.vpn_manager.metrics_screen.domain.modal.MetricsEntity

interface MetricsRepository {
    suspend fun getServerMetrics(serverId: String): MetricsEntity
}