package com.example.vpn_manager.modal_inbound.data.mapper

import com.example.vpn_manager.modal_inbound.data.model.AddEditInboundRequestDto
import com.example.vpn_manager.modal_inbound.data.model.InboundDetailDto
import com.example.vpn_manager.modal_inbound.domain.model.AddEditInboundEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

fun AddEditInboundEntity.toRequestDto(): AddEditInboundRequestDto {
    val clientId = UUID.randomUUID().toString()
    val email = generateRandomEmail()
    val subId = generateRandomSubId()
    val settings = """
        {
            "clients": [
                {
                    "id": "$clientId",
                    "flow": "",
                    "email": "$email",
                    "limitIp": 0,
                    "totalGB": 0,
                    "expiryTime": 0,
                    "enable": true,
                    "tgId": "",
                    "subId": "$subId",
                    "comment": "",
                    "reset": 0
                }
            ],
            "decryption": "none",
            "encryption": "none"
        }
    """.trimIndent()
    val streamSettings = """
        {
            "network": "xhttp",
            "security": "reality",
            "externalProxy": [],
            "realitySettings": {
                "show": false,
                "xver": 0,
                "target": "ya.ru:443",
                "serverNames": ["ya.ru", "www.ya.ru"],
                "privateKey": "aOHDKEadKzrgk9acDETLZarFWD21AqudFnzfzV8ggm0",
                "minClientVer": "",
                "maxClientVer": "",
                "maxTimediff": 0,
                "shortIds": ["65", "37db14d77445", "ac5a201f92", "4cecac9a6b134b", "ac7d6015", "140e", "ac5dde", "85c5d6576ac598a4"],
                "mldsa65Seed": "",
                "settings": {
                    "publicKey": "2ewlIV4JYviPEZcSfGLz1lQJBLb6zwi5wli3VmegQ3U",
                    "fingerprint": "chrome",
                    "serverName": "",
                    "spiderX": "/",
                    "mldsa65Verify": ""
                }
            },
            "xhttpSettings": {
                "path": "/",
                "host": "",
                "headers": {},
                "scMaxBufferedPosts": 30,
                "scMaxEachPostBytes": "1000000",
                "scStreamUpServerSecs": "20-80",
                "noSSEHeader": false,
                "xPaddingBytes": "100-1000",
                "mode": "auto"
            }
        }
    """.trimIndent()
    val sniffing = """
        {
            "enabled": true,
            "destOverride": ["http", "tls", "quic", "fakedns"],
            "metadataOnly": true,
            "routeOnly": true
        }
    """.trimIndent()
    val allocate = """
        {
            "strategy": "always",
            "refresh": 5,
            "concurrency": 3
        }
    """.trimIndent()

    return AddEditInboundRequestDto(
        remark = this.remark,
        enable = this.enable,
        expiryTime = 0,
        listen = "",
        port = this.port.toIntOrNull() ?: 443,
        protocol = "vless",
        settings = settings,
        streamSettings = streamSettings,
        sniffing = sniffing,
        allocate = allocate,
        up = 0,
        down = 0,
        total = 0
    )
}

fun InboundDetailDto.toEntity(): AddEditInboundEntity {
    return AddEditInboundEntity(
        id = this.id,
        remark = this.remark,
        enable = this.enable,
        protocol = this.protocol,
        port = this.port.toString(),
    )
}

private fun generateRandomEmail(): String {
    val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
    return (1..8).map { chars.random() }.joinToString("")
}

private fun generateRandomSubId(): String {
    val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
    return (1..16).map { chars.random() }.joinToString("")
}