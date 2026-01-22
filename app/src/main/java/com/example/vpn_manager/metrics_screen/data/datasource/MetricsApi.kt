package com.example.vpn_manager.metrics_screen.data.datasource
import com.example.vpn_manager.metrics_screen.data.model.ServerStatusDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface MetricsApi {
    @GET
    suspend fun getServerStatus(
        @Url url: String,
        @Header("Cookie") cookies: String? = null
    ): ServerStatusDto
}