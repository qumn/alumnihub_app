package xyz.qumn.alumnihub_app.screen.fleamarket;

import xyz.qumn.alumnihub_app.screen.fleamarket.module.Goods
import java.math.BigDecimal
import kotlin.random.Random

data class pageParam(
    val pageSize: Int,
    val pageNum: Int,
)

object GoodsApi {
    fun page(pageParam: pageParam): List<Goods> {
        val goods = mutableListOf<Goods>()
        for (i in 1..pageParam.pageSize) {
            goods.add(
                Goods(
                    name = "GTX 1060Ti",
                    cover = randomCover(),
                    price = BigDecimal.valueOf(99.99),
                    sellerId = 1,
                    sellerAvatar = "https://placekitten.com/200/287",
                    sellerName = "Seller"
                )
            )
        }
        return goods
    }
}


fun randomCover(): String {
    val imgs = listOf(
        "https://placehold.co/600x400/000000/FFFFFF/png",
        "https://placehold.co/300x600/000000/FFFFFF/png",
        "https://placehold.co/500x400/000000/FFFFFF/png"
    )
    return imgs[Random.nextInt(0, imgs.size)]
}