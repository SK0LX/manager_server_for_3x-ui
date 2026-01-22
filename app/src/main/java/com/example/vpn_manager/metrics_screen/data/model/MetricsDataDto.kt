package com.example.vpn_manager.metrics_screen.data.model

import com.google.gson.annotations.SerializedName


data class MetricsDataDto(
    val onlineServers: Int = 0,
    val totalServers: Int = 0,
    val totalClients: Int = 0,
    val activeConnections: Int = 0,
    val cpuUsage: Int = 0,
    val memoryUsage: Int = 0,
    val diskUsage: Int = 0,
    val downloadTraffic: Long = 0,
    val uploadTraffic: Long = 0,
    val totalTraffic: Long = 0,
    val avgLatency: Int = 0,
    val uptime: String = "0d 0h 0m",
    val errorRate: Double = 0.0,
    val tcpConnections: Int = 0,
    val udpConnections: Int = 0,
    val uploadSpeed: Long = 0,
    val downloadSpeed: Long = 0,
    val cpuCores: Int = 0,
    val logicalProcessors: Int = 0,
    val cpuSpeedMhz: Double = 0.0,
    val publicIpv4: String = "",
    val publicIpv6: String = "",
    val xrayVersion: String = "",
    val xrayState: String = "",
    val xrayErrorMsg: String = "",
    val loadAverage: String = "",
    val memoryCurrent: Long = 0,
    val memoryTotal: Long = 0,
    val diskCurrent: Long = 0,
    val diskTotal: Long = 0,
    val swapCurrent: Long = 0,
    val swapTotal: Long = 0,
    val appThreads: Int = 0,
    val appMemory: Long = 0,
    val appUptime: Int = 0
)

data class ServerStatusDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("obj")
    val obj: ServerStatusObjDto?
)

data class ServerStatusObjDto(
    @SerializedName("cpu")
    val cpu: Double?,

    @SerializedName("cpuCores")
    val cpuCores: Int?,

    @SerializedName("logicalPro")
    val logicalPro: Int?,

    @SerializedName("cpuSpeedMhz")
    val cpuSpeedMhz: Double?,

    @SerializedName("mem")
    val mem: ServerMemDto?,

    @SerializedName("swap")
    val swap: ServerSwapDto?,

    @SerializedName("disk")
    val disk: ServerDiskDto?,

    @SerializedName("xray")
    val xray: ServerXrayDto?,

    @SerializedName("uptime")
    val uptime: Int?,

    @SerializedName("loads")
    val loads: List<Double>?,

    @SerializedName("tcpCount")
    val tcpCount: Int?,

    @SerializedName("udpCount")
    val udpCount: Int?,

    @SerializedName("netIO")
    val netIO: ServerNetIODto?,

    @SerializedName("netTraffic")
    val netTraffic: ServerNetTrafficDto?,

    @SerializedName("publicIP")
    val publicIP: ServerPublicIPDto?,

    @SerializedName("appStats")
    val appStats: ServerAppStatsDto?
)

data class ServerMemDto(
    @SerializedName("current")
    val current: Long?,

    @SerializedName("total")
    val total: Long?
)

data class ServerSwapDto(
    @SerializedName("current")
    val current: Long?,

    @SerializedName("total")
    val total: Long?
)

data class ServerDiskDto(
    @SerializedName("current")
    val current: Long?,

    @SerializedName("total")
    val total: Long?
)

data class ServerXrayDto(
    @SerializedName("state")
    val state: String?,

    @SerializedName("errorMsg")
    val errorMsg: String?,

    @SerializedName("version")
    val version: String?
)

data class ServerNetIODto(
    @SerializedName("up")
    val up: Long?,

    @SerializedName("down")
    val down: Long?
)

data class ServerNetTrafficDto(
    @SerializedName("sent")
    val sent: Long?,

    @SerializedName("recv")
    val recv: Long?
)

data class ServerPublicIPDto(
    @SerializedName("ipv4")
    val ipv4: String?,

    @SerializedName("ipv6")
    val ipv6: String?
)

data class ServerAppStatsDto(
    @SerializedName("threads")
    val threads: Int?,

    @SerializedName("mem")
    val mem: Long?,

    @SerializedName("uptime")
    val uptime: Int?
)