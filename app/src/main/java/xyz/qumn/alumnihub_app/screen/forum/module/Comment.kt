package xyz.qumn.alumnihub_app.screen.forum.module

import xyz.qumn.alumnihub_app.module.User
import java.time.LocalDateTime

data class Comment(
    val id: Long,
    val pid: Long, // post id
    val content: String, // the content of comment
    val commenter: User,
    val thumpUpCount: Int,
    val createAt: LocalDateTime,
    val parent: Comment? = null,
    val replays: List<Comment> // the replays of the comment
) {
    fun flatReplay(): List<Comment> {
        val rst = mutableListOf<Comment>()
        val stack = ArrayDeque<Comment>()
        stack.addAll(this.replays)
        while (stack.size > 0) {
            val cur = stack.removeFirst()
            rst.add(cur.copy(replays = listOf()))
            for (replay in cur.replays) {
                stack.addLast(replay.copy(parent = cur))
            }
        }
        return rst;
    }
}