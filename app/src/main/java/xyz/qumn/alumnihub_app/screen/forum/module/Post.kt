package xyz.qumn.alumnihub_app.screen.forum.module

import java.time.LocalDateTime

data class Post(
    val creator: Long,
    val creatorName: String,
    val creatorAvatar: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val tags: List<String>,
)
