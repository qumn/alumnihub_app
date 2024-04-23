package xyz.qumn.alumnihub_app.screen.fleamarket.module;

import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.module.User

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
    val seller: User,
    val goods: Goods,
)

@Serializable
data class Goods(
    val desc: String,
    val imgs: List<String>,
    val price: Int,
)