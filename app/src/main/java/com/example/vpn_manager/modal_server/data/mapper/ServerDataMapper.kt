package com.example.vpn_manager.modal_server.data.mapper

import com.example.vpn_manager.modal_server.data.model.AddEditServerRequestDto
import com.example.vpn_manager.modal_server.data.model.LoginResponseCookies
import com.example.vpn_manager.modal_server.domain.model.AddEditServerEntity
import com.example.vpn_manager.storage.model.StorageEntity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

fun AddEditServerEntity.toRequestDto(): AddEditServerRequestDto {
    return AddEditServerRequestDto(
        name = this.name,
        username = this.username,
        password = this.password,
        url = this.url
    )
}

fun Response<ResponseBody>.toCookies(): LoginResponseCookies {
    val headers = this.headers()
    val token = headers.values("Set-Cookie")
    val cookiePairs = token.map { cookieHeader ->
        cookieHeader.split(';').firstOrNull()?.trim() ?: cookieHeader
    }
    val combinedCookies = cookiePairs.joinToString("; ")
    println("Извлеченные куки: $combinedCookies")
    return LoginResponseCookies(
        cookies = combinedCookies,
    )
}

fun StorageEntity.toAddEditServerEntity() : AddEditServerEntity{
    return AddEditServerEntity(
        id = this.id,
        name = this.name,
        url = this.url,
        username = this.username,
        password = this.password,
        cookies = this.cookies
    )
}
