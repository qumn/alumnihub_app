package xyz.qumn.alumnihub_app.req

import kotlinx.serialization.Serializable


interface PageParam {
    val pageNo: Int
    val pageSize: Int
}

@Serializable
data class Page<T>(
    val list: List<T>,
    val pageSize: Int = 10,
    val total: Long? = null,
) {
    companion object {
        fun <T> empty(): Page<T> = Page(listOf())
        fun <T> from(list: List<T>): Page<T> {
            return Page(list)
        }

        fun <T> from(vararg post: T): Page<T> {
            return from(post.toList())
        }
    }
}
