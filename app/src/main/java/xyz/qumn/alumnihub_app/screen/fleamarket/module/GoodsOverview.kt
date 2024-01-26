package xyz.qumn.alumnihub_app.screen.fleamarket.module;

import xyz.qumn.alumnihub_app.module.User
import java.math.BigDecimal

data class GoodsOverview(
    val name: String,
    val cover: String,
    val price: BigDecimal,

    val sellerId: Long,
    val sellerAvatar: String,
    val sellerName: String,
)

data class GoodsDetail(
    val name: String,
    val desc: String,
    val imgs: List<String>,
    val price: BigDecimal,

    val seller: User,
)