package xyz.qumn.alumnihub_app.screen.fleamarket.module;

import java.math.BigDecimal

data class Goods(
    val name: String,
    val imgs: List<String>,
    val price: BigDecimal,

    val sellerId: Long
)
