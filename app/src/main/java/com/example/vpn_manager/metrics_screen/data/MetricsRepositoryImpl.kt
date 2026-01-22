package com.example.vpn_manager.metrics_screen.data
import com.example.vpn_manager.metrics_screen.data.datasource.MetricsApi
import com.example.vpn_manager.metrics_screen.data.mapper.toEntity
import com.example.vpn_manager.metrics_screen.data.mapper.toMetricsDataDto
import com.example.vpn_manager.metrics_screen.domain.modal.MetricsEntity
import com.example.vpn_manager.metrics_screen.domain.repository.MetricsRepository
import com.example.vpn_manager.storage.repository.StorageRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetricsRepositoryImpl @Inject constructor(
    private val metricsApi: MetricsApi,
    private val storageRepository: StorageRepository
) : MetricsRepository {

    override suspend fun getServerMetrics(serverId: String): MetricsEntity {
        val storageServers = storageRepository.getServer(serverId)
        if (storageServers == null) {
            throw Exception("Server not found")
        }

        return metricsApi.getServerStatus(
            storageServers.url + "panel/api/server/status",
            storageServers.cookies
        ).toMetricsDataDto().toEntity()
    }
}