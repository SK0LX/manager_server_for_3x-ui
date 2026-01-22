package com.example.vpn_manager.modal_server.data.datasource

import com.example.vpn_manager.modal_server.data.model.AddEditServerRequestDto
import com.example.vpn_manager.modal_server.data.model.ServerDetailResponseDto
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface AddEditServerApi {
    @POST
    suspend fun login(@Body body: AddEditServerRequestDto, @Url url: String): Response<ResponseBody>
}