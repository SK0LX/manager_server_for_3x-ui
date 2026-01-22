package com.example.vpn_manager.inbounds_screen.domain.model

data class InboundEntity(
    val id : String,
    val name : String,
    val port : String,
    val protocols : String,
    val clients: String,
    val traffic : String,
    val dataTime : String,
    val status: String
)