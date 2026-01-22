package com.example.vpn_manager.inbounds_screen.data.datasouce

import kotlinsandbox.feature.login.data.model.DeleteInboundResponseDto
import kotlinsandbox.feature.login.data.model.InboundListResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface InboundsApi {
    @GET
    suspend fun getInbounds(
        @Url fullUrl: String,
        @Header("Cookie") cookies: String? = null
    ): InboundListResponseDto


    @POST
    suspend fun deleteInbound(
        @Url fullUrl: String,
        @Header("Cookie") cookies: String? = null
    ): DeleteInboundResponseDto
}