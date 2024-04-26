package xyz.qumn.alumnihub_app.screen.forum

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.AluSnackbarHost
import xyz.qumn.alumnihub_app.AppState
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.composable.ImgGrid
import xyz.qumn.alumnihub_app.screen.forum.module.Post
import xyz.qumn.alumnihub_app.screen.forum.module.PostPagingSource
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
import xyz.qumn.alumnihub_app.ui.theme.Blue80
import xyz.qumn.alumnihub_app.ui.theme.Gray20
import xyz.qumn.alumnihub_app.ui.theme.Gray50
import xyz.qumn.alumnihub_app.ui.theme.Gray80
import xyz.qumn.alumnihub_app.util.toViewFormat
import java.time.Instant


class ForumViewModel : ViewModel() {
    private val _postRsp: MutableStateFlow<PagingData<Post>> =
        MutableStateFlow(PagingData.empty())
    var postRsp = _postRsp.asStateFlow()
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Pager(
                config = PagingConfig(10, enablePlaceholders = true)
            ) {
                PostPagingSource()
            }.flow.cachedIn(viewModelScope).collect {
                _postRsp.value = it
            }
        }
    }
}

@Composable
fun ForumScreen(
    forumViewModel: ForumViewModel = viewModel(),
    onClickPostCard: (postId: Long) -> Unit
) {
    val postPage = forumViewModel.postRsp.collectAsLazyPagingItems()

    Scaffold(
        snackbarHost = { AluSnackbarHost() },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState((postPage.loadState.refresh is LoadState.Loading && postPage.itemCount > 0)),
            onRefresh = { postPage.refresh() },
        ) {
            Column(Modifier.padding(it)) {
                LazyColumn(Modifier.background(Color.White)) {
                    items(postPage.itemCount) { idx ->
                        val post = postPage[idx]
                        if (post == null) {
                            Text(text = "no post to show")
                            return@items
                        }
                        Log.d("post", post.toString())
                        PostItem(Modifier, post = post) {
                            onClickPostCard(post.id)
                        }
                        if (idx != postPage.itemCount - 1) { // not last, draw a divider
                            HorizontalDivider(
                                modifier = Modifier.padding(20.dp, 3.dp),
                                thickness = 1.6.dp,
                                color = Gray50
                            )
                        }

                    }
                    if (postPage.loadState.append is LoadState.Loading) { //下一页的load状态
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
                            }
                        }
                    }
                }
            }
        }

        if (postPage.loadState.refresh is LoadState.Loading) {
            if (postPage.itemCount == 0) {//第一次响应页面加载时的loading状态
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
                }
            }
        } else if (postPage.loadState.refresh is LoadState.Error) {
            //加载失败的错误页面
            Box(modifier = Modifier.fillMaxSize()) {
                Button(modifier = Modifier.align(alignment = Alignment.Center),
                    onClick = { postPage.refresh() }) {
                    Text(text = "加载失败！请重试")
                }
            }
        }
    }
}

@Composable
fun PostItem(modifier: Modifier = Modifier, post: Post, onClick: () -> Unit) {
    val titleStyle = MaterialTheme.typography.titleMedium
    val contentStyle = MaterialTheme.typography.bodyMedium

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RectangleShape,
    ) {
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
        Spacer(modifier = Modifier.width(12.dp))
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
//    val modifier = Modifier.size(12.dp)
    val size = 13

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Row {
            IconTextButton(
                Icons.Outlined.ThumbUp,
                "${post.thumbUpCount}",
                background = Gray20,
                onClick = { })
            IconTextButton(Icons.Outlined.Forum, "${post.commentCount} 条评论", onClick = { })
        }
        IconTextButton(Icons.Outlined.MoreHoriz, onClick = { })
    }
}

@Composable
fun IconTextButton(
    icon: ImageVector,
    text: String = "",
    background: Color = Color.Transparent,
    clicked: Boolean = false,
    onClick: () -> Unit = { }
) {
    val color = if (clicked) Blue80 else Gray80
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(4.dp, 0.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(background)
            .padding(4.dp, 1.dp)
            .clickable { onClick() }
    ) {
        Icon(icon, contentDescription = null, Modifier.size(15.dp), tint = color)
        Spacer(modifier = Modifier.width(2.dp))
        Text(text, fontSize = 2.4.em, color = color)
    }
}

@Preview
@Composable
fun ForumScreenPreview() {
    AppState.ProvideAppState {
        Alumnihub_appTheme {
            ForumScreen() {}
        }
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
        Instant.now(),
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
