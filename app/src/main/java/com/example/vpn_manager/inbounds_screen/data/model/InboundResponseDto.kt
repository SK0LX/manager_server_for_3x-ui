package kotlinsandbox.feature.login.data.model

import com.google.gson.annotations.SerializedName


data class InboundListResponseDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("obj")
    val obj: List<InboundDto>?
)

data class InboundDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("remark")
    val remark: String,

    @SerializedName("port")
    val port: Int,

    @SerializedName("protocol")
    val protocol: String,

    @SerializedName("up")
    val up: Long,

    @SerializedName("down")
    val down: Long,

    @SerializedName("total")
    val total: Long,

    @SerializedName("clientStats")
    val clientStats: List<ClientStatsDto>?,

    @SerializedName("enable")
    val enable: Boolean,

    @SerializedName("expiryTime")
    val expiryTime: Long,

    @SerializedName("lastTrafficResetTime")
    val lastTrafficResetTime: Long,

    @SerializedName("settings")
    val settings: String,

    @SerializedName("streamSettings")
    val streamSettings: String,

    @SerializedName("tag")
    val tag: String,

    @SerializedName("sniffing")
    val sniffing: String
)

data class ClientStatsDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("uuid")
    val uuid: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("enable")
    val enable: Boolean,

    @SerializedName("up")
    val up: Long,

    @SerializedName("down")
    val down: Long,

    @SerializedName("total")
    val total: Long,

    @SerializedName("expiryTime")
    val expiryTime: Long,
    @SerializedName("lastOnline")
    val lastOnline: Long
)

data class DeleteInboundResponseDto(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("msg")
    val msg: String,

    @SerializedName("obj")
    val obj: Int?
)