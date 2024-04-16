package xyz.qumn.alumnihub_app.api

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.ApiClient.client


@Serializable
data class LoginReq(
    val username: String,
    val password: String
)

object UserApi {
    suspend fun login(username: String, password: String): String {
        return client.post("/security/login") {
            setBody(LoginReq(username, password))
        }.body<Rsp<String>>().data!!
    }
}