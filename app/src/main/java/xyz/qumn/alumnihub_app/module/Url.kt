package xyz.qumn.alumnihub_app.module

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class URN(val name: String)

@Serializable
data class URL(
    val urn: URN,
    val domain: String,
    val protocol: String,
) {
    val location: String
        get() = "${protocol}://${domain}/${urn.name}"
}