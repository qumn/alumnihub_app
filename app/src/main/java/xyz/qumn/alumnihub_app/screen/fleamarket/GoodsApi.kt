package xyz.qumn.alumnihub_app.screen.fleamarket;

import xyz.qumn.alumnihub_app.screen.fleamarket.module.Goods
import java.math.BigDecimal

data class pageParam(
    val pageSize: Int,
    val pageNum: Int,
)

object GoodsApi {
    fun page(pageParam: pageParam) : List<Goods> {
        val goods = mutableListOf<Goods>()
        for (i in 1..pageParam.pageSize) {
            goods.add(Goods(
                name = "GTX 1060Ti",
                imgs = listOf(
                    "https://placehold.co/600x400/000000/FFFFFF/png",
                    "https://placehold.co/300x600/000000/FFFFFF/png",
                    "https://placehold.co/500x400/000000/FFFFFF/png"
                ),
                price = BigDecimal.valueOf(99.99),
                sellerId = 1
            ))
        }
        return goods
    }
}
