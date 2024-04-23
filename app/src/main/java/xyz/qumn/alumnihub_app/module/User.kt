package xyz.qumn.alumnihub_app.module

import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.InstantSerializer
import java.time.Instant


@Serializable
data class User(
    val uid: Long,
    val name: String,
    val avatar: String,
    val gender: Gender,
    @Serializable(with = InstantSerializer::class)
    val birthDay: Instant?,
    val phone: String,
    val email: String?,
)

enum class Gender {
    FEMALE, MALE, UNKNOWN
}