package xyz.qumn.alumnihub_app.screen.lostfound.module

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.ApiClient
import xyz.qumn.alumnihub_app.req.Page
import xyz.qumn.alumnihub_app.req.PageParam

@Serializable
class LostItemPageParam(
    override val pageNo: Int = 1,
    override val pageSize: Int = 10,
) : PageParam

object LostItemApi {
    suspend fun page(param: LostItemPageParam): Page<LostItem> =
        ApiClient.page("/lostfound/search", param)

}

object LostItemPagingSource : PagingSource<Int, LostItem>() {
    override fun getRefreshKey(state: PagingState<Int, LostItem>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LostItem> {
        val pageNo = params.key ?: 1
        val lostItemPage = LostItemApi.page(LostItemPageParam(pageNo))
        return try {
            LoadResult.Page(
                data = lostItemPage.list,
                prevKey = if (pageNo == 1) null else pageNo.minus(1),
                nextKey = if (lostItemPage.list.isEmpty()) null else pageNo.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
