package xyz.qumn.alumnihub_app.screen.fleamarket.module;

import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.BigDecimalSerializer
import xyz.qumn.alumnihub_app.module.URL
import xyz.qumn.alumnihub_app.module.User
import java.math.BigDecimal

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

data class GoodsDetail(
    val name: String,
    val desc: String,
    val imgs: List<String>,
    val price: Int,

    val seller: User,
)