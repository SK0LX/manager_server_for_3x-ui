package com.example.vpn_manager.modal_inbound.data.datasource

import com.example.vpn_manager.modal_inbound.data.model.AddEditInboundRequestDto
import com.example.vpn_manager.modal_inbound.data.model.InboundDetailResponseDto
import kotlinsandbox.feature.login.data.model.InboundListResponseDto
import okhttp3.Cookie
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface AddEditApi {
    @GET
    suspend fun getInbound(@Url url: String, @Header("Cookie") cookie: String?): InboundDetailResponseDto

    @POST
    suspend fun addInbound(@Url url: String, @Header("Cookie") cookie: String?, @Body body :  AddEditInboundRequestDto): InboundDetailResponseDto

    @POST
    suspend fun updateInbound(@Url url: String, @Header("Cookie") cookie: String?, @Body body : AddEditInboundRequestDto ): InboundDetailResponseDto
}