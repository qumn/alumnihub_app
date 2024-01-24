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
                    "https://placekitten.com/200/287",
                    "https://placekitten.com/201/287",
                    "https://placekitten.com/202/287"
                ),
                price = BigDecimal.valueOf(99.99),
                sellerId = 1
            ))
        }
        return goods
    }
}
