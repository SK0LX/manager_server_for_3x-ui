package com.example.vpn_manager.network

import com.example.vpn_manager.clients_screen.data.datasource.ClientsApi
import com.example.vpn_manager.inbounds_screen.data.datasouce.InboundsApi
import com.example.vpn_manager.metrics_screen.data.datasource.MetricsApi
import com.example.vpn_manager.modal_client.data.datasource.AddEditClientApi
import com.example.vpn_manager.modal_inbound.data.datasource.AddEditApi
import com.example.vpn_manager.modal_server.data.datasource.AddEditServerApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkProvidesModule{

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient = provideHttpClient()
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideClientsApi(retrofit: Retrofit): ClientsApi {
        return retrofit.create(ClientsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInboundsApi(retrofit: Retrofit): InboundsApi {
        return retrofit.create(InboundsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideModalClientApi(retrofit: Retrofit): AddEditClientApi {
        return retrofit.create(AddEditClientApi::class.java)
    }

    @Provides
    @Singleton
    fun provideModalInboundApi(retrofit: Retrofit): AddEditApi {
        return retrofit.create(AddEditApi::class.java)
    }

    @Provides
    @Singleton
    fun provideModalServerApi(retrofit: Retrofit): AddEditServerApi {
        return retrofit.create(AddEditServerApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMetricsApi(retrofit: Retrofit): MetricsApi {
        return retrofit.create(MetricsApi::class.java)
    }
}