package com.example.vpn_manager.modal_client.data.mapper

import com.example.vpn_manager.modal_client.data.model.ClientDetailDto
import com.example.vpn_manager.modal_client.data.model.InboundResponse
import com.example.vpn_manager.modal_client.data.model.UpdateClientRequestDto
import com.example.vpn_manager.modal_client.domain.model.AddEditClientEntity
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.util.UUID

fun ClientDetailDto.toEntity(): AddEditClientEntity {
    return AddEditClientEntity(
        id = this.uuid,
        inboundId = this.inboundId,
        email = this.email,
        enable = this.enable,
        totalGB = this.totalGB,
        expiryTime = this.expiryTime,
    )
}

fun AddEditClientEntity.toUpdateRequestDto(): UpdateClientRequestDto {
    val inboundIdInt = this.inboundId.toIntOrNull() ?: throw IllegalArgumentException("Invalid inboundId")

    return UpdateClientRequestDto(
        id = inboundIdInt,
        settings = this.toSettingsJson()
    )
}

fun AddEditClientEntity.toSettingsJson(): String {
    val gson = Gson()
    val clientUuid = this.id ?: UUID.randomUUID().toString()

    val clientObject = JsonObject().apply {
        addProperty("id", clientUuid)
        addProperty("flow", "")
        addProperty("email", email)
        addProperty("limitIp", 0)
        addProperty("totalGB", totalGB)
        addProperty("expiryTime", expiryTime)
        addProperty("enable", enable)
        addProperty("tgId", "")
        addProperty("subId", generateRandomString(16))
        addProperty("comment", "")
        addProperty("reset", 0)

        // Для новых клиентов устанавливаем временные метки
        val currentTime = System.currentTimeMillis()
        addProperty("created_at", currentTime)
        addProperty("updated_at", currentTime)
    }

    val clientsArray = JsonArray()
    clientsArray.add(clientObject)
    val settingsObject = JsonObject()
    settingsObject.add("clients", clientsArray)

    return gson.toJson(settingsObject)
}

fun generateRandomString(length: Int): String {
    val charset = "abcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

fun InboundResponse.toClientDetailDto(inboundId: String, clientId: String) : ClientDetailDto?{
    if (!this.success)
        return null

    val inbound = this.obj?.find { it.id == inboundId.toIntOrNull() }
        ?: return null

    val settingsJson = inbound.settings
    val gson = Gson()
    val settings = gson.fromJson(settingsJson, JsonObject::class.java)
    val clientsArray = settings.getAsJsonArray("clients")
    val clientDetail = clientsArray?.find { clientElement ->
        val clientObj = clientElement.asJsonObject
        val clientEmail = clientObj.get("email").asString
        val clientUuid = clientObj.get("id").asString
        clientUuid == clientId || clientEmail == clientId
    }?.asJsonObject ?: return null
    val uuid = clientDetail.get("id").asString
    val clientStat = inbound.clientStats?.find { it.uuid == uuid }
        ?: return null
    return ClientDetailDto(
        id = clientStat.id.toString(),
        uuid = clientStat.uuid,
        inboundId = inboundId,
        email = clientStat.email,
        enable = clientStat.enable,
        totalGB = clientStat.total,
        expiryTime = clientStat.expiryTime,
        flow = clientDetail.get("flow").asString,
        limitIp = clientDetail.get("limitIp").asInt,
        comment = clientDetail.get("comment").asString,
        reset = clientDetail.get("reset").asLong,
        createdAt = clientDetail.get("created_at")?.asLong ?: 0L,
        updatedAt = clientDetail.get("updated_at")?.asLong ?: 0L,
        subId = clientDetail.get("subId").asString,
        tgId = clientDetail.get("tgId").asString,
        up = clientStat.up,
        down = clientStat.down,
        lastOnline = clientStat.lastOnline
    )
}