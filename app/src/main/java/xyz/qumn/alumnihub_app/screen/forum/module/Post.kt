package xyz.qumn.alumnihub_app.screen.forum.module

import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.InstantSerializer
import java.time.Instant

@Serializable
data class Post(
    val id: Long,
    val creator: Long,
    val creatorName: String,
    val creatorAvatar: String,
    val title: String,
    val content: String,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    val tags: List<String>,
    val imgs: List<String> = emptyList(),
    val thumbUpCount: Int,
    val commentCount: Int,
    val shareCount: Int,
) {
    val comments: List<Comment>
        get() = PostApi.getComments(this.id)
}
