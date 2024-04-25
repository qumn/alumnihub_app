package xyz.qumn.alumnihub_app.screen.forum

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.R
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.composable.CompactField
import xyz.qumn.alumnihub_app.module.Gender
import xyz.qumn.alumnihub_app.module.User
import xyz.qumn.alumnihub_app.screen.forum.module.Comment
import xyz.qumn.alumnihub_app.screen.forum.module.Post
import xyz.qumn.alumnihub_app.screen.forum.module.PostApi
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
import xyz.qumn.alumnihub_app.ui.theme.Blue90
import xyz.qumn.alumnihub_app.ui.theme.Gray60
import xyz.qumn.alumnihub_app.util.toViewFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


@Composable
fun PostDetailScreen(pid: Long?, onClickBack: () -> Unit) {
    var isLoading: Boolean by remember { mutableStateOf(true) }
    var titleMsg: String by remember { mutableStateOf("加载中") }
    var post: Post? by remember { mutableStateOf(null) }
    SideEffect {
        CoroutineScope(Dispatchers.IO).launch {
            if (pid == null) return@launch
            PostApi.queryBy(pid).onSuccess {
                post = it
                titleMsg = it.title
            }.onFailure {
                titleMsg = "帖子找不到了"
            }
            isLoading = false
        }
    }
    Scaffold(
        topBar = { TopBar(title = titleMsg, onClickBack) },
        bottomBar = { BottomBar() },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.ime)
            .exclude(WindowInsets.navigationBars)
    ) {
        Surface(modifier = Modifier.padding(it)) {
            if (post == null) {
                NotFound(titleMsg)
            } else {
                PostDetailContent(post!!)
            }
        }
    }

}

@Composable
private fun PostDetailContent(post: Post) {
    val titleStyle = MaterialTheme.typography.titleMedium
    LazyColumn {
        item {
            Text(post.title, style = titleStyle)
        }
        item {
            CreatorInfo(post = post)
        }
        item {
            Text(text = post.content)
        }
        items(post.imgs) { img ->
            Image(img)
        }
        item {
            CommentView(comments = post.comments)
        }
    }
}

@Composable
private fun NotFound(msg: String) {
    val textStyle = MaterialTheme.typography.titleLarge
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(msg, style = textStyle)
    }
}

@Composable
fun CreatorInfo(post: Post) {
    val nameStyle = MaterialTheme.typography.titleSmall

    Row(verticalAlignment = Alignment.CenterVertically) {
        Avatar(
            url = post.creatorAvatar,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "by")
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = post.creatorName, style = nameStyle)
    }
}

@Composable
private fun Image(img: String, padding: Int = 3) {
    val modifier = Modifier.padding(padding.dp)
    AsyncImage(
        model = img,
        modifier = modifier,
        placeholder = painterResource(id = R.drawable.placeholder),
        error = painterResource(id = R.drawable.placeholder),
        contentDescription = "image"
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, onClickBack: () -> Unit) {
    TopAppBar(
        title = {
            Title(title)
        },
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(Icons.Filled.ArrowBackIosNew, null)
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomBar() {
    var comment by remember { mutableStateOf("") }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompactField(
            value = comment,
            onValueChange = { comment = it },
            leadingIcon = {
                Icon(Icons.Outlined.Edit, contentDescription = "edit")
            },
            modifier = Modifier
                .background(
                    Color.White,
                    RoundedCornerShape(percent = 50)
                )
                .padding(4.dp)
                .weight(4f)
                .height(32.dp),
            fontSize = 12.sp,
            placeholderText = "写点什么"
        )

        if (WindowInsets.isImeVisible) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Publish, null)
            }
        } else {
            val modifier = Modifier.weight(2f)
            IconText(modifier, icon = Icons.Outlined.ThumbUp, "20")
            IconText(modifier, icon = Icons.Outlined.Star, "2")
            IconText(modifier, icon = Icons.Outlined.AddComment, "19")
        }
    }
}

@Composable
private fun IconText(modifier: Modifier, icon: ImageVector, text: String) {
    val textStyle = MaterialTheme.typography.titleMedium
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(icon, modifier = Modifier.size(32.dp), contentDescription = null)
        Spacer(modifier = Modifier.width(2.dp))
        Text(text, style = textStyle)
    }
}

@Composable
fun Title(str: String) {
    Text(
        str,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun CommentView(comments: List<Comment>) {
    for (comment in comments) {
        CommentItem(comment = comment)
    }
}

@Composable
fun CommentItem(comment: Comment, avatarSize: Int = 42, th: Int = -1) {
    var expand by rememberSaveable { mutableStateOf(false) }
    val flatReplays = comment.flatReplay().sortedBy { it.createAt }

    CommentView(comment, 42) {
        Replays(replays = flatReplays, expand) {
            expand = true
        }
    }
}

@Composable
fun Replays(replays: List<Comment>, expand: Boolean = false, onclick: () -> Unit) {
    val showMoreStyle = MaterialTheme.typography.labelMedium.copy(color = Gray60)
    val displayReplays = if (!expand) {
        replays.take(1)
    } else {
        replays
    }
    val modifier = Modifier.padding(0.dp, 2.dp, 0.dp, 0.dp)
    Column(modifier) {
        for (replay in displayReplays) {
            CommentView(comment = replay, avatarSize = 24)
        }
    }
    if (!expand && replays.size > 1) {
        Text(
            text = "展开 ${replays.size - 1} 条回复",
            Modifier.clickable { onclick() },
            style = showMoreStyle
        )
    }
}

@Composable
fun CommentView(
    comment: Comment,
    avatarSize: Int = 42,
    indentComponent: @Composable () -> Unit = {}
) {
    val commenter = comment.commenter
    val nameStyle = MaterialTheme.typography.titleMedium.copy(
        color = Gray60
    )
    val timeStyle = MaterialTheme.typography.titleMedium.copy(
        color = Gray60
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        Avatar(
            url = commenter.avatar, modifier = Modifier
                .size(avatarSize.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 10.dp, 0.dp)
            ) {
                Row {
                    Text(text = commenter.name, style = nameStyle)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "\u00B7", style = timeStyle)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = comment.createAt.toViewFormat(), style = timeStyle)
                }
                IconTextButton(
//                    Modifier.size(14.dp),
                    Icons.Outlined.ThumbUp,
                    "${comment.thumpUpCount}",
                    onClick = { })
            }
            Row {
                if (comment.parent != null) {
                    Text("回复 ")
                    Text(comment.parent.commenter.name, style = nameStyle)
                    Text(text = " : ")
                }
                Text(text = comment.content)
            }
            indentComponent()
        }
    }

}

@Composable
//@Preview
fun CommentItemPreview() {
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

    Alumnihub_appTheme {
        Card {
            CommentItem(comment)
        }
    }
}


@Composable
@Preview
fun postDetailPrevie() {
    PostDetailScreen(pid = 1) {
    }
}