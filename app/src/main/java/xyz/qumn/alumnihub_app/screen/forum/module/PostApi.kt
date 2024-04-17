package xyz.qumn.alumnihub_app.screen.forum.module

import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import xyz.qumn.alumnihub_app.api.ApiClient
import xyz.qumn.alumnihub_app.module.Gender
import xyz.qumn.alumnihub_app.module.User
import xyz.qumn.alumnihub_app.req.PageParam
import xyz.qumn.alumnihub_app.req.PageRst
import java.time.LocalDateTime


data class PostPageParam(
    override var pageSize: Int = 10,
    override var lastId: Long = 0
) : PageParam

@Serializable
data class PostCreateReq(
    val title: String,
    val price: String,
    val imgs: List<String> = emptyList(),
)

object PostApi

suspend fun PostApi.createNewPost(post: PostCreateReq): Result<Long> {
    return ApiClient.post("/posts") {
        setBody(post)
    }
}

fun PostApi.page(postPageParam: PostPageParam): PageRst<Post> {
    return PageRst.of(
        listOf(
            Post(
                id = 1,
                creator = 1L,
                creatorName = "Creator 1",
                creatorAvatar = "https://example.com/avatar1.png",
                title = "Post 1",
                content = "This is the content of Post 1",
                createdAt = LocalDateTime.now(),
                tags = listOf("tag1", "tag2"),
                thumbUpCount = 10,
                commentCount = 5,
                shareCount = 2,
                imgs = listOf(
                    "https://picsum.photos/200/300",
                ),
            ),
            Post(
                id = 2,
                creator = 2L,
                creatorName = "Creator 2",
                creatorAvatar = "https://example.com/avatar2.png",
                title = "Post 2",
                content = "This is the content of Post 2",
                createdAt = LocalDateTime.now(),
                tags = listOf("tag3", "tag4"),
                thumbUpCount = 20,
                commentCount = 10,
                shareCount = 4,
                imgs = listOf(
                    "https://picsum.photos/200/300",
                    "https://picsum.photos/300/300",
                    "https://picsum.photos/400/300",
                ),
            ),
            Post(
                id = 3,
                creator = 3L,
                creatorName = "Creator 3",
                creatorAvatar = "https://example.com/avatar3.png",
                title = "Post 3",
                content = "This is the content of Post 3",
                createdAt = LocalDateTime.now(),
                tags = listOf("tag5", "tag6"),
                thumbUpCount = 30,
                commentCount = 15,
                shareCount = 6,
                imgs = listOf(
                    "https://picsum.photos/200/300",
                    "https://picsum.photos/300/300",
                    "https://picsum.photos/400/300",
                    "https://picsum.photos/400/400",
                ),
            )
        )
    )
}

fun PostApi.getById(id: Long): Post {
    return Post(
        1,
        1,
        "张三",
        "https://placekitten.com/201/287",
        "锚定发展目标 明晰落实举措 展现更大作为——学校干部职工谈落实春季干部大会精神",
        "When naming variables in your code, it's best to be descriptive and precise about what the variable represents. For example, thumbUps or likeCount could both represent the number of thumbs-up or likes that a post or comment has received. Here",
        LocalDateTime.now(),
        tags = listOf("键盘", "科技"),
        imgs = listOf(
            "https://placekitten.com/201/287",
            "https://placekitten.com/201/287",
            "https://placekitten.com/201/287",
            "https://placekitten.com/201/287",
            "https://placekitten.com/201/287",
        ),
        thumbUpCount = 302,
        commentCount = 64,
        shareCount = 22,
    )
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
        createAt = LocalDateTime.now(),
        replays = listOf(),
        thumpUpCount = 3
    )
    val yeasDay = LocalDateTime.now().minusDays(1)
    val replay1 = comment.copy(replays = listOf(comment.copy(createAt = yeasDay), comment.copy()))

    comment = comment.copy(replays = listOf(replay1, comment.copy(), comment.copy()))
    return listOf(comment.copy(), comment.copy(), comment.copy())
}