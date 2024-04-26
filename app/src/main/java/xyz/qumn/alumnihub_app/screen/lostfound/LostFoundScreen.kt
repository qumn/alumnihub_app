package xyz.qumn.alumnihub_app.screen.lostfound

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.AluSnackbarHost
import xyz.qumn.alumnihub_app.R
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.screen.lostfound.module.LostItem
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
                LostItemPagingSource
            }.flow.cachedIn(viewModelScope).collect {
                _lostItemRsp.value = it
            }
        }
    }
}

@Composable
fun LostFoundScreen(lostFoundViewModel: LostFoundViewModel = viewModel()) {
    val lostItems = lostFoundViewModel.lostItemRsp.collectAsLazyPagingItems()

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
                    LostItemCompose(lostItem) {}
                }
            }
        }
    }
}

@Composable
fun LostItemCompose(
    lostItem: LostItem,
    modifier: Modifier = Modifier,
    onClickLostItem: (Long) -> Unit
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
                    .clickable { onClickLostItem(lostItem.id) },
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
                    verticalAlignment = Alignment.Bottom
                ) {
                    PublisherInfo(
                        lostItem.publisherAvatar,
                        lostItem.publisherName
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row {
                        Icon(Icons.Filled.LocationOn, contentDescription = null)
                        Text(text = "凤雏食堂")
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
        publishAt = Instant.now()
    )
    LostItemCompose(lostItem) {}
}