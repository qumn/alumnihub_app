package xyz.qumn.alumnihub_app.screen.lostfound.module

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.request.setBody
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
    suspend fun publishMissingItem(missingItemReq: PublishMissingItemReq) : Result<Long> {
        return ApiClient.post<Long>("/lostfound") {
            setBody(missingItemReq)
        }
    }
    suspend fun page(param: LostItemPageParam): Page<LostItem> =
        ApiClient.page("/lostfound/search", param)

    suspend fun claim(id: Long, answers: List<String>) : Result<Boolean> {
        return ApiClient.put<Boolean>("/lostfound/$id/claim") {
            setBody(answers)
        }
    }

}

class LostItemPagingSource : PagingSource<Int, LostItem>() {
    override fun getRefreshKey(state: PagingState<Int, LostItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

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



@Serializable
data class Question(
    val content: String,
    val answer: String,
) {
    companion object {
        fun empty(): Question {
            return Question("", "")
        }
    }
}

@Serializable
data class PublishMissingItemReq(
    val name: String,
    val location: String,
    val questions: List<Question>,
    val imgs: List<String>
) {
    companion object {
        fun empty(): PublishMissingItemReq =
            PublishMissingItemReq("", "", emptyList(), emptyList())
    }
    fun validate() : String? {
        if (name.isBlank()) {
            return "请描述一下物品"
        }
        if (location.isBlank()) {
            return "请填写拾取地址"
        }
        if (questions.isEmpty()) {
            return "请设置相关问题"
        }
        if (imgs.isEmpty()) {
            return "请最少上传一张物品图片"
        }
        return null;
    }
}
