package xyz.qumn.alumnihub_app.screen.fleamarket;

import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.ApiClient
import xyz.qumn.alumnihub_app.module.Gender
import xyz.qumn.alumnihub_app.module.URL
import xyz.qumn.alumnihub_app.module.User
import xyz.qumn.alumnihub_app.req.PageParam
import xyz.qumn.alumnihub_app.screen.fleamarket.module.GoodsDetail
import xyz.qumn.alumnihub_app.screen.fleamarket.module.GoodsOverview
import java.math.BigDecimal
import kotlin.random.Random

data class GoodsPageParam(
    override var pageSize: Int = 10,
    override var lastId: Long = 0
) : PageParam

@Serializable
data class CreateGoodsReq(
    val desc: String,
    val imgs: List<String> = emptyList(),
    val price: String,
)

object GoodsApi {
    suspend fun publish(
        desc: String,
        price: String,
        imgs: List<URL> = emptyList()
    ): Result<Long> {
        return ApiClient.post("/trades") {
            setBody(
                CreateGoodsReq(
                    desc = desc,
                    price = price,
                    imgs = imgs.map { it.urn.name }
                )
            )
        }
    }

    fun page(pageParam: GoodsPageParam): List<GoodsOverview> {
        val goods = mutableListOf<GoodsOverview>()
        for (i in 1..pageParam.pageSize) {
            goods.add(
                GoodsOverview(
                    id = 1,
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

    fun get(id: Long): GoodsDetail {
        return GoodsDetail(
            "name",
            desc = "desc desc desc desc desc desc desc desc desc desc desc desc desc desc ",
            listOf(
                "https://picsum.photos/200/300",
                "https://picsum.photos/200/300",
                "https://picsum.photos/200/300"
            ),
            100.toBigDecimal(),
            User(
                1L,
                "name",
                avatar = "https://picsum.photos/201/300",
                Gender.UNKNOWN,
                null,
                "",
                ""
            )
        )
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