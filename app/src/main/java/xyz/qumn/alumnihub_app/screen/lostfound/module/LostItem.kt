package xyz.qumn.alumnihub_app.screen.lostfound.module

import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.InstantSerializer
import java.time.Instant

@Serializable
data class LostItem(
    val id: Long,
    val img: String,
    val title: String,
    val location: String,
    val questions: List<String>,

    val publisherId: Long,
    val publisherAvatar: String,
    val publisherName: String,
    @Serializable(with = InstantSerializer::class)
    val publishAt: Instant
)
