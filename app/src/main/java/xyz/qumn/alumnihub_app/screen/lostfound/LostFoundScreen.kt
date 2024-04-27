package xyz.qumn.alumnihub_app.screen.lostfound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.AluSnackbarHost
import xyz.qumn.alumnihub_app.R
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.composable.useSnack
import xyz.qumn.alumnihub_app.screen.lostfound.module.LostItem
import xyz.qumn.alumnihub_app.screen.lostfound.module.LostItemApi
import xyz.qumn.alumnihub_app.screen.lostfound.module.LostItemPagingSource
import xyz.qumn.alumnihub_app.util.toViewFormat
import java.time.Instant

class LostFoundViewModel : ViewModel() {
    private val _lostItemRsp: MutableStateFlow<PagingData<LostItem>> =
        MutableStateFlow(PagingData.empty())

    var lostItemRsp = _lostItemRsp.asStateFlow()
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Pager(
                config = PagingConfig(10, enablePlaceholders = true)
            ) {
                LostItemPagingSource()
            }.flow.cachedIn(viewModelScope).collect {
                _lostItemRsp.value = it
            }
        }
    }
}

@Composable
fun LostFoundScreen(lostFoundViewModel: LostFoundViewModel = viewModel()) {
    val snackBarHelper = useSnack()
    val lostItems = lostFoundViewModel.lostItemRsp.collectAsLazyPagingItems()
    var answerItem by remember {
        mutableStateOf<LostItem?>(null)
    }

    Scaffold(
        snackbarHost = { AluSnackbarHost() },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            SwipeRefresh(
                state = rememberSwipeRefreshState((lostItems.loadState.refresh is LoadState.Loading && lostItems.itemCount > 0)),
                onRefresh = { lostItems.refresh() },
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(2.dp, 12.dp)
                ) {
                    items(lostItems.itemCount) { idx ->
                        val lostItem = lostItems[idx]
                        if (lostItem == null) {
                            Text("no goods to show")
                            return@items
                        }
                        LostItemCompose(lostItem) {
                            answerItem = it
                        }
                    }
                }
            }

            if (lostItems.loadState.refresh is LoadState.Loading) {
                if (lostItems.itemCount == 0) {//第一次响应页面加载时的loading状态
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(alignment = Alignment.Center))
                    }
                }
            } else if (lostItems.loadState.refresh is LoadState.Error) {
                //加载失败的错误页面
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(modifier = Modifier.align(alignment = Alignment.Center),
                        onClick = { lostItems.refresh() }) {
                        Text(text = "加载失败！请重试")
                    }
                }
            }
        }
        AnswerQuestionDialog(
            show = answerItem != null,
            { answerItem!!.questions },
            close = { answerItem = null }) { answers ->
            val id = answerItem!!.id
            CoroutineScope(Dispatchers.IO).launch {
                LostItemApi.claim(id, answers).onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        snackBarHelper.show("成功")
                    }
                    answerItem = null
                }.onFailure {
                    CoroutineScope(Dispatchers.Main).launch {
                        snackBarHelper.show("回答错误")
                    }
                    answerItem = null
                }
            }
        }
    }
}

@Composable
fun LostItemCompose(
    lostItem: LostItem,
    modifier: Modifier = Modifier,
    onClickClaim: (LostItem) -> Unit
) {
    val titleStyle = MaterialTheme.typography.titleMedium

    Card(modifier.padding(3.dp)) {
        Column(Modifier.fillMaxWidth()) {
            AsyncImage(
                model = lostItem.img,
                contentDescription = "lost item image",
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth()
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 6.dp),
            ) {
                Text(text = lostItem.title, style = titleStyle)
                Spacer(Modifier.height(4.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PublisherInfo(
                        lostItem.publisherAvatar,
                        lostItem.publisherName
                    )
                    TextButton(onClick = { onClickClaim(lostItem) }) {
                        Text(text = "认领")
                    }
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.LocationOn,
                            modifier = Modifier.size(12.dp),
                            contentDescription = null
                        )
                        Text(text = lostItem.location, style = MaterialTheme.typography.labelSmall)
                    }
                    Text(
                        text = lostItem.publishAt.toViewFormat(),
                    )
                }
            }
        }
    }

}

@Composable
private fun PublisherInfo(avatar: String, name: String) {
    Row(verticalAlignment = Alignment.Bottom) {
        Avatar(
            url = avatar,
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun AnswerQuestionDialog(
    show: Boolean,
    getQuestions: () -> List<String>,
    close: () -> Unit,
    confirm: (List<String>) -> Unit
) {
    if (show) {
        val questions = getQuestions()
        // set the default answer to empty
        val titlePlaceHolderTextStyle = MaterialTheme.typography.titleMedium
        val answers = remember { mutableStateListOf(*questions.map { "" }.toTypedArray()) }
        AlertDialog(
            title = { Text(text = "问题") },
            text = {
                for ((idx, question) in questions.withIndex()) {
                    Column {
                        Text(text = "${idx + 1}. $question")
                        TextField(
                            value = answers[idx],
                            onValueChange = {
                                answers[idx] = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    "请输入答案",
                                    style = titlePlaceHolderTextStyle,
                                    color = Color.Black.copy(.6f)
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }
                }
            },
            onDismissRequest = { close() },
            confirmButton = {
                Button(onClick = { confirm(answers) }) {
                    Text(text = "确定")
                }
            }
        )
    }
}

@Preview
@Composable
fun LostItemPreview() {
    var lostItem = LostItem(
        id = 1,
        title = "GTX 1060Ti",
        img = "https://placekitten.com/200/287",

        publisherId = 1,
        publisherAvatar = "https://placekitten.com/201/287",
        publisherName = "张三",
        location = "凤雏食堂",
        questions = listOf(),
        publishAt = Instant.now()
    )
    LostItemCompose(lostItem) {}
}