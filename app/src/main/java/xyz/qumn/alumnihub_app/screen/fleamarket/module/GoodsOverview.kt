package xyz.qumn.alumnihub_app.screen.fleamarket.module;

import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.module.User
import xyz.qumn.alumnihub_app.screen.forum.module.Comment
import xyz.qumn.alumnihub_app.screen.forum.module.CommentApi
import xyz.qumn.alumnihub_app.screen.forum.module.SubjectType

@Serializable
data class GoodsOverview(
    val id: Long,
    val desc: String,
    val cover: String,
    val price: Int,
    val sellerId: Long,
    val sellerAvatar: String,
    val sellerName: String,
)

@Serializable
data class TradeDetails(
    val id: Long,
    val seller: User,
    val goods: Goods,
    var comments: List<Comment> = listOf()
) {
    suspend fun loadComments() {
        this.comments = CommentApi.getBy(SubjectType.Trade, id)
    }
}

@Serializable
data class Goods(
    val desc: String,
    val imgs: List<String>,
    val price: Int,
)