package com.example.vpn_manager.clients_screen.data.datasource

import com.example.vpn_manager.clients_screen.data.model.ClientsResponseDto
import com.example.vpn_manager.clients_screen.data.model.DeleteClientResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url

interface ClientsApi {

    @GET
    suspend fun getInboundsWithClients(
        @Url url: String,
        @Header("Cookie") cookies: String? = null
    ): ClientsResponseDto

    @POST
    suspend fun deleteClient(
        @Url url: String,
        @Header("Cookie") cookies: String? = null
    ): DeleteClientResponseDto
}