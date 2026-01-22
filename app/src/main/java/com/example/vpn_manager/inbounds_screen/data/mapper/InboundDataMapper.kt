// InboundDataMapper.kt
package com.example.vpn_manager.inbounds.data.mapper

import com.example.vpn_manager.inbounds_screen.domain.model.InboundEntity
import kotlinsandbox.feature.login.data.model.ClientStatsDto
import kotlinsandbox.feature.login.data.model.InboundDto
import kotlinsandbox.feature.login.data.model.InboundListResponseDto
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.map

fun InboundListResponseDto.toInboundListEntity(): List<InboundEntity> {

    if (this.obj.isNullOrEmpty()){
        throw Exception("Пустой инбаунд")
    }
    return this.obj.map { inboundDto ->
        inboundDto.toEntity()
    }
}

fun InboundDto.toEntity(): InboundEntity {
    return InboundEntity(
        id = this.id.toString(),
        name = this.remark.ifEmpty { "Inbound ${this.port}" },
        port = this.port.toString(),
        protocols = this.protocol,
        clients = formatClients(this.clientStats),
        traffic = formatTraffic(this.up, this.down, this.total),
        status = getStatus(this.enable, this.expiryTime),
        dataTime = formatDate(this.lastTrafficResetTime)
    )
}

private fun formatClients(clientStats: List<ClientStatsDto>?): String {
    return if (clientStats.isNullOrEmpty()) {
        "0 клиентов"
    } else {
        val activeCount = clientStats.count { it.enable }
        "${clientStats.size} клиентов ($activeCount активных)"
    }
}

private fun formatTraffic(up: Long, down: Long, total: Long): String {
    fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1073741824 -> "${bytes / 1073741824} GB"
            bytes >= 1048576 -> "${bytes / 1048576} MB"
            bytes >= 1024 -> "${bytes / 1024} KB"
            else -> "$bytes B"
        }
    }

    return "↑${formatBytes(up)} ↓${formatBytes(down)} (${formatBytes(total)})"
}

private fun getStatus(enable: Boolean, expiryTime: Long): String {
    val now = System.currentTimeMillis() / 1000

    return when {
        !enable -> "Отключен"
        expiryTime > 0 && expiryTime < now -> "Истек"
        else -> "Активен"
    }
}

private fun formatDate(timestamp: Long): String {
    if (timestamp == 0L) return "Никогда"

    val date = Date(timestamp * 1000)
    val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return sdf.format(date)
}