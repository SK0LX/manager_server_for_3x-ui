package com.example.vpn_manager.modal_inbound.presentation.model

data class AddEditInboundState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val id: Int? = null,
    val remark: String = "",
    val enable: Boolean = true,
    val protocol: String = "vless",
    val port: String = "",
    val listen: String = "",
    val total: String = "0",
    val trafficReset: String = "never",
    val expiryTime: String = "",

    val clientCount: String = "1",
    val clientId: String = "",
    val clientEmail: String = "",
    val clientTotalGB: String = "0",
    val clientLimitIp: String = "0",
    val clientEnable: Boolean = true,

    val decryption: String = "none",
    val encryption: String = "none",

    val network: String = "tcp",
    val security: String = "none",
    val acceptProxyProtocol: Boolean = false,

    val sniffingEnabled: Boolean = false,
    val destOverride: List<String> = listOf("http", "tls", "quic", "fakedns"),

    val allocateStrategy: String = "always",
    val refresh: String = "5",
    val concurrency: String = "3",

    val protocols: List<String> = listOf("vless", "vmess", "trojan", "shadowsocks", "dokodemo-door"),
    val networks: List<String> = listOf("tcp", "kcp", "ws", "http", "quic", "grpc"),
    val securities: List<String> = listOf("none", "tls", "reality"),
    val trafficResets: List<String> = listOf("never", "daily", "weekly", "monthly", "yearly"),

    val isEditMode: Boolean = false,

    var serverId: String? = null,
    var inboundId: Int? = null
)