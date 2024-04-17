package xyz.qumn.alumnihub_app.api

import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable


@Serializable
data class LoginReq(
    val username: String,
    val password: String
)

object UserApi {
    suspend fun login(username: String, password: String): Result<String> {
        return ApiClient.post("/security/login") {
            setBody(LoginReq(username, password))
        }
    }
}