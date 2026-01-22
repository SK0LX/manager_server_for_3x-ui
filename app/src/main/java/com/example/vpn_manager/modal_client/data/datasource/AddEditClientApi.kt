package com.example.vpn_manager.modal_client.data.datasource

import com.example.vpn_manager.modal_client.data.model.*
import okhttp3.RequestBody
import retrofit2.http.*

interface AddEditClientApi {

    @GET
    suspend fun getInbounds(
        @Url fullUrl: String,
        @Header("Cookie") cookies: String? = null
    ): InboundResponse

    @POST
    suspend fun addClient(
        @Url fullUrl: String,
        @Body settings: AddClientRequestDto,
        @Header("Cookie") cookies: String? = null
    ): AddClientResponseDto

    @POST
    suspend fun updateClient(
        @Url fullUrl: String,
        @Body request: UpdateClientRequestDto,
        @Header("Cookie") cookies: String? = null
    ): ClientDetailResponseDto
}