package xyz.qumn.alumnihub_app.api;

import kotlinx.serialization.Serializable

@Serializable
data class Rsp<T>(
    val code: Int,
    val msg: String,
    val data: T?,
) {
    val success: Boolean
        get() = code == 0
}
