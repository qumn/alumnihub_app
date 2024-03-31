package xyz.qumn.alumnihub_app.req


interface PageParam {
    var pageSize: Int;
    var lastId: Long
}
data class PageRst<T>(
    val list: List<T>
) {
    companion object {
        fun <T> of(list: List<T>): PageRst<T> {
            return PageRst(list)
        }

        fun <T> of(vararg post: T) : PageRst<T> {
            return of(post.toList())
        }
    }
}
