package com.example.vpn_manager.clients_screen.data.mapper

import com.example.vpn_manager.clients_screen.data.model.ClientResponseDto
import com.example.vpn_manager.clients_screen.data.model.ClientsResponseDto
import com.example.vpn_manager.clients_screen.domain.model.ClientsEntity
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ClientResponseDto.toClientsEntity(): ClientsEntity {
    val isOnlineText = formatLastOnline(this.lastOnline)
    return ClientsEntity(
        id = this.id.toString(),
        name = this.email,
        isWork = this.enable,
        isOnline = isOnlineText,
        traffic = formatTraffic(this.up, this.down),
        dateEnd = formatExpiryTime(this.expiryTime),
        subscribeLink = "",
        uuid = this.uuid
    )
}

private fun formatLastOnline(lastOnline: Long): String {
    if (lastOnline <= 0) return "Никогда"

    val currentTime = System.currentTimeMillis()
    val diffMinutes = (currentTime - lastOnline) / (1000 * 60)

    return when {
        diffMinutes < 1 -> "Online"
        diffMinutes < 60 -> "${diffMinutes.toInt()} мин назад"
        diffMinutes < 1440 -> "${(diffMinutes / 60).toInt()} ч назад"
        else -> "${(diffMinutes / 1440).toInt()} дн назад"
    }
}

private fun formatTraffic(up: Long, down: Long): List<String> {
    val format = DecimalFormat("#.##")

    fun formatBytes(bytes: Long): String {
        return when {
            bytes >= 1024 * 1024 * 1024 -> "${format.format(bytes / (1024.0 * 1024 * 1024))} GB"
            bytes >= 1024 * 1024 -> "${format.format(bytes / (1024.0 * 1024))} MB"
            bytes >= 1024 -> "${format.format(bytes / 1024.0)} KB"
            else -> "$bytes B"
        }
    }

    return listOf(formatBytes(up), formatBytes(down))
}

private fun formatExpiryTime(expiryTime: Long): String {
    if (expiryTime <= 0) return "Бессрочно"

    val expiryDate = Date(expiryTime)
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    return formatter.format(expiryDate)
}

fun ClientsResponseDto.toListClientsEntity(
    inboundId: String
): List<ClientsEntity>{
    val inbound = this.obj?.find { it?.id.toString() == inboundId }
    val clients = inbound?.clientStats ?: emptyList()
    return clients.map { it.toClientsEntity() }
}