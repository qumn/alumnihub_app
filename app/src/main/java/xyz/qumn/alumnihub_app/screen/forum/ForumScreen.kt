package xyz.qumn.alumnihub_app.screen.forum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.screen.forum.module.Post
import java.time.LocalDateTime

@Composable
fun ForumScreen() {

}

@Composable
fun PostItem(modifier: Modifier = Modifier, post: Post) {
    Card(modifier) {
        Row {
            Avatar(url = post.creatorAvatar)
            Text(text = post.creatorName)
        }
        Column {
            Text(text = post.title)
            for (tag in post.tags) {
                Text(text = tag)
            }
        }
    }


}

@Preview
@Composable
fun ForumScreenPreview() {
    ForumScreen()
}

@Preview(backgroundColor = 0xffffff)
@Composable
fun PostItemPreview() {
    val post = Post(
        1,
        "zs",
        "https://placekitten.com/201/287",
        "This is Title",
        "content content content contentcontent contentcontent content",
        LocalDateTime.now(),
        tags = listOf("键盘", "科技")
    )
    PostItem(Modifier.width(200.dp), post)
}