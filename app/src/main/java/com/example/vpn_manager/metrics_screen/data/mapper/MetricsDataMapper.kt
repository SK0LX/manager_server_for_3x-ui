package com.example.vpn_manager.metrics_screen.data.mapper

import com.example.vpn_manager.metrics_screen.domain.modal.MetricsEntity
import com.example.vpn_manager.metrics_screen.data.model.MetricsDataDto
import com.example.vpn_manager.metrics_screen.data.model.ServerStatusDto
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

fun MetricsDataDto.toEntity(): MetricsEntity {
    return MetricsEntity(
        onlineServers = this.onlineServers,
        totalServers = this.totalServers,
        totalClients = this.totalClients,
        activeConnections = this.activeConnections,
        cpuUsage = this.cpuUsage,
        memoryUsage = this.memoryUsage,
        diskUsage = this.diskUsage,
        downloadTraffic = this.downloadTraffic,
        uploadTraffic = this.uploadTraffic,
        totalTraffic = this.totalTraffic,
        avgLatency = this.avgLatency,
        uptime = this.uptime,
        errorRate = this.errorRate,
        tcpConnections = this.tcpConnections,
        udpConnections = this.udpConnections,
        uploadSpeed = this.uploadSpeed,
        downloadSpeed = this.downloadSpeed,
        cpuCores = this.cpuCores,
        publicIpv4 = this.publicIpv4,
        publicIpv6 = this.publicIpv6,
        xrayVersion = this.xrayVersion,
        xrayState = this.xrayState,
        loadAverage = this.loadAverage,
        memoryCurrent = this.memoryCurrent,
        memoryTotal = this.memoryTotal,
        diskCurrent = this.diskCurrent,
        diskTotal = this.diskTotal,
        swapCurrent = this.swapCurrent,
        swapTotal = this.swapTotal
    )
}

fun formatTraffic(bytes: Long): String {
    val format = DecimalFormat("#.##")
    return when {
        bytes >= 1024L * 1024 * 1024 * 1024 -> "${format.format(bytes / (1024.0 * 1024 * 1024 * 1024))} TB"
        bytes >= 1024L * 1024 * 1024 -> "${format.format(bytes / (1024.0 * 1024 * 1024))} GB"
        bytes >= 1024L * 1024 -> "${format.format(bytes / (1024.0 * 1024))} MB"
        bytes >= 1024 -> "${format.format(bytes / 1024.0)} KB"
        else -> "$bytes B"
    }
}

fun formatSpeed(bytes: Long): String {
    return formatTraffic(bytes) + "/s"
}

fun formatMemory(bytes: Long?): String {
    if (bytes == null) return "0 B"
    val format = DecimalFormat("#.##")
    return when {
        bytes >= 1024L * 1024 * 1024 -> "${format.format(bytes / (1024.0 * 1024 * 1024))} GB"
        bytes >= 1024L * 1024 -> "${format.format(bytes / (1024.0 * 1024))} MB"
        bytes >= 1024 -> "${format.format(bytes / 1024.0)} KB"
        else -> "$bytes B"
    }
}

fun ServerStatusDto.toMetricsDataDto(): MetricsDataDto {
    val obj = this.obj
    val loadAverageStr = if (obj?.loads != null && obj.loads.isNotEmpty()) {
        obj.loads.joinToString(", ") { "%.2f".format(it) }
    } else {
        "0, 0, 0"
    }

    return MetricsDataDto(
        onlineServers = if (success && obj != null) 1 else 0,
        totalServers = 1,
        totalClients = obj?.appStats?.threads ?: 0,
        activeConnections = (obj?.tcpCount ?: 0) + (obj?.udpCount ?: 0),
        cpuUsage = (obj?.cpu ?: 0.0).toInt(),  // Конвертируем Double в Int
        memoryUsage = calculatePercentage(obj?.mem?.current, obj?.mem?.total),
        diskUsage = calculatePercentage(obj?.disk?.current, obj?.disk?.total),
        downloadTraffic = obj?.netTraffic?.recv ?: 0,
        uploadTraffic = obj?.netTraffic?.sent ?: 0,
        totalTraffic = (obj?.netTraffic?.recv ?: 0) + (obj?.netTraffic?.sent ?: 0),
        avgLatency = 0,
        uptime = formatUptime(obj?.uptime ?: 0),
        errorRate = 0.0,
        tcpConnections = obj?.tcpCount ?: 0,
        udpConnections = obj?.udpCount ?: 0,
        uploadSpeed = obj?.netIO?.up ?: 0,
        downloadSpeed = obj?.netIO?.down ?: 0,
        cpuCores = obj?.cpuCores ?: 0,
        logicalProcessors = obj?.logicalPro ?: 0,
        cpuSpeedMhz = obj?.cpuSpeedMhz ?: 0.0,
        publicIpv4 = obj?.publicIP?.ipv4 ?: "",
        publicIpv6 = obj?.publicIP?.ipv6 ?: "",
        xrayVersion = obj?.xray?.version ?: "",
        xrayState = obj?.xray?.state ?: "",
        xrayErrorMsg = obj?.xray?.errorMsg ?: "",
        loadAverage = loadAverageStr,
        memoryCurrent = obj?.mem?.current ?: 0,
        memoryTotal = obj?.mem?.total ?: 0,
        diskCurrent = obj?.disk?.current ?: 0,
        diskTotal = obj?.disk?.total ?: 0,
        swapCurrent = obj?.swap?.current ?: 0,
        swapTotal = obj?.swap?.total ?: 0,
        appThreads = obj?.appStats?.threads ?: 0,
        appMemory = obj?.appStats?.mem ?: 0,
        appUptime = obj?.appStats?.uptime ?: 0
    )
}

private fun calculatePercentage(current: Long?, total: Long?): Int {
    return if (current != null && total != null && total > 0) {
        ((current.toDouble() / total.toDouble()) * 100).toInt()
    } else {
        0
    }
}

private fun formatUptime(seconds: Int): String {
    val days = TimeUnit.SECONDS.toDays(seconds.toLong())
    val hours = TimeUnit.SECONDS.toHours(seconds.toLong()) - TimeUnit.DAYS.toHours(days)
    val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong()) -
            TimeUnit.DAYS.toMinutes(days) -
            TimeUnit.HOURS.toMinutes(hours)

    return when {
        days > 0 -> "${days}d ${hours}h ${minutes}m"
        hours > 0 -> "${hours}h ${minutes}m"
        else -> "${minutes}m"
    }
}