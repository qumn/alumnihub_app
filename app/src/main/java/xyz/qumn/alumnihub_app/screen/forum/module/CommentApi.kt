package xyz.qumn.alumnihub_app.screen.forum.module

import xyz.qumn.alumnihub_app.module.Gender
import xyz.qumn.alumnihub_app.module.User
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit


enum class SubjectType {
    Trade, Forum, LostFound
}

class CreateComment() {
}

object CommentApi {
   suspend fun getBy(subjectType: SubjectType, subjectId: Long) : List<Comment> {
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

}
