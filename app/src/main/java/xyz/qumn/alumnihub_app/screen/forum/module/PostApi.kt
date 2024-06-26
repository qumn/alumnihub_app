package xyz.qumn.alumnihub_app.screen.forum.module

import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.ApiClient
import xyz.qumn.alumnihub_app.module.Gender
import xyz.qumn.alumnihub_app.module.URL
import xyz.qumn.alumnihub_app.module.User
import xyz.qumn.alumnihub_app.req.Page
import xyz.qumn.alumnihub_app.req.PageParam
import java.time.Instant
import java.time.temporal.ChronoUnit


class PostPageParam(
    override val pageNo: Int = 1,
    override val pageSize: Int = 10
) : PageParam

@Serializable
data class PostCreateReq(
    val title: String,
    val content: String,
    val imgs: List<String> = emptyList(),
) {
    companion object {
        fun empty(): PostCreateReq =
            PostCreateReq(
                "",
                "",
                emptyList()
            )

    }

    fun validate(): String? {
        if (title.isBlank()){
            return "标题不能为空"
        }
        if (content.isBlank()){
            return "内容不能为空"
        }
        if (imgs.isEmpty()){
            return "最少上传一个图片"
        }
        return null
    }
}

object PostApi {
    suspend fun createNewPost(post: PostCreateReq): Result<Long> =
        ApiClient.post("/posts") {
            setBody(post)
        }

    suspend fun page(postPageParam: PostPageParam): Page<Post> =
        ApiClient.page("/posts/search", postPageParam)

    suspend fun queryBy(id: Long): Result<Post> =
        ApiClient.get("/posts/${id}")
}

class PostPagingSource : PagingSource<Int, Post>() {

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: 1
        val postPage = PostApi.page(PostPageParam(page))
        return try {
            LoadResult.Page(
                data = postPage.list,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (postPage.list.isEmpty()) null else page.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

fun PostApi.getComments(pid: Long): List<Comment> {
    val user = User(
        1L,
        "张三",
        avatar = "https://picsum.photos/201/300",
        Gender.UNKNOWN,
        null,
        "",
        ""
    )
    var comment = Comment(
        1L,
        1L,
        "这是一条评论",
        commenter = user,
        createAt = Instant.now(),
        replays = listOf(),
        thumpUpCount = 3
    )
    val yeasDay = Instant.now().minus(1, ChronoUnit.DAYS)
    val replay1 = comment.copy(replays = listOf(comment.copy(createAt = yeasDay), comment.copy()))

    comment = comment.copy(replays = listOf(replay1, comment.copy(), comment.copy()))
    return listOf(comment.copy(), comment.copy(), comment.copy())
}