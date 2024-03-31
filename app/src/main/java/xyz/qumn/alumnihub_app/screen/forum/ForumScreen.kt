package xyz.qumn.alumnihub_app.screen.forum

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.composable.ImgGrid
import xyz.qumn.alumnihub_app.screen.forum.module.Post
import xyz.qumn.alumnihub_app.screen.forum.module.PostApi
import xyz.qumn.alumnihub_app.screen.forum.module.PostPageParam
import xyz.qumn.alumnihub_app.screen.forum.module.page
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
import xyz.qumn.alumnihub_app.util.toViewFormat
import java.time.LocalDateTime

@Composable
fun ForumScreen(onClickPostCard: (postId: Long) -> Unit) {
    val postPage = PostApi.page(PostPageParam())
    Column {
        for (post in postPage.list) {
            Log.d("post", post.toString())
            PostItem(Modifier.padding(4.dp, 2.dp), post = post) {
                onClickPostCard(post.id)
            }
        }
    }
}

@Composable
fun PostItem(modifier: Modifier = Modifier, post: Post, onClick: () -> Unit) {
    val titleStyle = MaterialTheme.typography.titleMedium
    val contentStyle = MaterialTheme.typography.bodyMedium

    Card(modifier.clickable { onClick() }) {
        Column(Modifier.padding(6.dp, 4.dp)) {
            PostItemHeader(post)
            Text(
                text = post.title,
                style = titleStyle,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = post.content,
                style = contentStyle,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
            ImgGrid(imgs = post.imgs)
            Spacer(modifier = Modifier.height(4.dp))
            PostItemTags(post.tags)
            Spacer(modifier = Modifier.height(4.dp))
            PostItemFooter(post)
        }
    }
}

@Composable
fun PostItemHeader(post: Post) {
    val nameStyle = MaterialTheme.typography.titleSmall
    val timeStyle = MaterialTheme.typography.labelSmall

    Row(verticalAlignment = Alignment.CenterVertically) {
        Avatar(
            url = post.creatorAvatar,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.SpaceEvenly) {
            Text(text = post.creatorName, style = nameStyle)
            Text(text = post.createdAt.toViewFormat(), style = timeStyle)
        }
    }
}

@Composable
fun PostItemTags(tags: List<String>) {
    val tagStyle = MaterialTheme.typography.labelSmall

    Row {
        for (tag in tags) {
            Text("#${tag}", style = tagStyle)
            Spacer(modifier = Modifier.width(2.dp))
        }
    }

}

@Composable
fun PostItemFooter(post: Post) {
    val modifier = Modifier.size(12.dp)

    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        IconTextButton(modifier, Icons.Outlined.ThumbUp, "${post.thumbUpCount}", onClick = { })
        IconTextButton(modifier, Icons.Outlined.Message, "${post.commentCount}", onClick = { })
        IconTextButton(modifier, Icons.Outlined.Share, "${post.shareCount}", onClick = { })
    }
}

@Composable
fun IconTextButton(
    modifier: Modifier,
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = { }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }) {
        Icon(icon, contentDescription = null, modifier)
        Spacer(modifier = Modifier.width(2.dp))
        Text(text, fontSize = 2.em)
    }
}

@Preview
@Composable
fun ForumScreenPreview() {
    Alumnihub_appTheme {
        ForumScreen(){}
    }
}

@Preview
@Composable
fun PostItemPreview() {
    val post = Post(
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
    Alumnihub_appTheme {
        PostItem(
            Modifier
                .background(Color.Cyan)
                .width(200.dp), post
        ) {}
    }
}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp,
) = layout { measurable, constraints ->
    // Measure the composable
    val placeable = measurable.measure(constraints)

    // Check the composable has a first baseline
    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    // Height of the composable with padding - first baseline
    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY
    layout(placeable.width, height) {
        // Where the composable gets placed
        placeable.placeRelative(0, placeableY)
    }
}

