package xyz.qumn.alumnihub_app.screen.fleamarket.module;

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.ApiClient
import xyz.qumn.alumnihub_app.module.URL
import xyz.qumn.alumnihub_app.req.Page
import xyz.qumn.alumnihub_app.req.PageParam
import kotlin.random.Random


@Serializable
data class CreateGoodsReq(
    val desc: String,
    val imgs: List<String> = emptyList(),
    val price: String,
)

@Serializable
class GoodsPageParam(
    override val pageNo: Int = 1,
    override val pageSize: Int = 10,
) : PageParam

object GoodsPagingSource : PagingSource<Int, GoodsOverview>() {
    override fun getRefreshKey(state: PagingState<Int, GoodsOverview>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GoodsOverview> {
        val pageNo = params.key ?: 1
        val goodsPage = GoodsApi.page(GoodsPageParam(pageNo))
        return try {
            LoadResult.Page(
                data = goodsPage.list,
                prevKey = if (pageNo == 1) null else pageNo.minus(1),
                nextKey = if (goodsPage.list.isEmpty()) null else pageNo.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}

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

    suspend fun page(param: GoodsPageParam): Page<GoodsOverview> =
        ApiClient.page("/trades/search", param)

    suspend fun get(id: Long) : Result<TradeDetails> =
        ApiClient.get("/trades/${id}")

}