package xyz.qumn.alumnihub_app.module

import java.time.Instant


data class User (
    val uid: Long,
    val name: String,
    val avatar: String,
    val gender: Gender,
    val birthDay: Instant?,
    val phone: String,
    val email: String?,
)

enum class Gender {
    FEMALE, MALE, UNKNOWN
}