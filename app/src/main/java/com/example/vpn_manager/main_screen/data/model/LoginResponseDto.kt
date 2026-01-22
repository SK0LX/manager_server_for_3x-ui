package kotlinsandbox.feature.login.data.model


data class ServerLoginResponse(
    val success: Boolean,
    val msg: String,
    val obj: Any? = null
)