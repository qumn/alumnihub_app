package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Card
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
import xyz.qumn.alumnihub_app.screen.fleamarket.module.GoodsOverview
import xyz.qumn.alumnihub_app.screen.fleamarket.module.GoodsPagingSource

class FleaMarketViewModel : ViewModel() {
    private val _goodsRsp: MutableStateFlow<PagingData<GoodsOverview>> =
        MutableStateFlow(PagingData.empty())

    var goodsRsp = _goodsRsp.asStateFlow()
        private set

    init {
        viewModelScope.launch(Dispatchers.IO) {
            Pager(
                config = PagingConfig(10, enablePlaceholders = true)
            ) {
                GoodsPagingSource
            }.flow.cachedIn(viewModelScope).collect {
                _goodsRsp.value = it
            }
        }
    }
}

@Composable
fun FleaMarketFlowScreen(
    fleaMarketViewModel: FleaMarketViewModel = viewModel(),
    onClickTradeCard: (tradeId: Long) -> Unit
) {
    val goodsOverviews = fleaMarketViewModel.goodsRsp.collectAsLazyPagingItems()

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
                items(goodsOverviews.itemCount) { idx ->
                    val goods = goodsOverviews[idx]
                    if (goods == null) {
                        Text("no goods to show")
                        return@items
                    }
                    FleaMarketCard(
                        goods = goods,
                        onClickTradeCard = onClickTradeCard
                    )
                }
            }
        }
    }

}

@Composable
fun FleaMarketCard(
    modifier: Modifier = Modifier,
    goods: GoodsOverview,
    onClickTradeCard: (tradeId: Long) -> Unit
) {
    val titleStyle = MaterialTheme.typography.titleMedium

    Card(modifier.padding(3.dp)) {
        Column(Modifier.fillMaxWidth()) {
            AsyncImage(
                model = goods.cover,
                contentDescription = "cover image",
                placeholder = painterResource(id = R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth()
                    .clickable { onClickTradeCard(goods.id) },
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 6.dp),
            ) {
                Text(text = goods.desc, style = titleStyle)
                Spacer(Modifier.height(4.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    PriceInfo(priceInCent = goods.price)
                    SellerInfo(goods.sellerAvatar, goods.sellerName)
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}


@Composable
private fun SellerInfo(avatar: String, name: String) {
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
fun FleaMarketCardPreview() {
    FleaMarketCard(
        modifier = Modifier
            .width(240.dp)
            .padding(8.dp),
        goods = GoodsOverview(
            id = 1,
            desc = "GTX 1060Ti",
            cover = "https://placekitten.com/200/287",
            price = 9999,
            sellerId = 1,
            sellerAvatar = "https://placekitten.com/201/287",
            sellerName = "张三"
        ),
        onClickTradeCard = {}
    )
}

@Preview
@Composable
fun FleaMarketFlowPreview() {
    FleaMarketFlowScreen {}
}

@Preview
@Composable
fun RespactRatio() {
    Image(
        painter = painterResource(id = R.drawable.placeholder),
        contentDescription = null,
        modifier = Modifier
            .width(20.dp)
            .aspectRatio(1F)
    )
}