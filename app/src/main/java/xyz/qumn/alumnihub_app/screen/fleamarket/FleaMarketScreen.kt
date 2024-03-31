package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import xyz.qumn.alumnihub_app.R
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.req.PageParam
import xyz.qumn.alumnihub_app.screen.fleamarket.module.GoodsOverview
import java.math.BigDecimal


@Composable
fun FleaMarketFlowScreen(onClickTradeCard: (tradeId: Long) -> Unit) {
    val goods = GoodsApi.page(GoodsPageParam())

    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
        items(goods.size) {
            FleaMarketCard(
                goods = goods[it],
                onClickTradeCard = onClickTradeCard
            )
        }
    }
}

@Composable
fun FleaMarketCard(modifier: Modifier = Modifier, goods: GoodsOverview, onClickTradeCard: (tradeId: Long) -> Unit) {
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
                Text(text = goods.name, style = titleStyle)
                Spacer(Modifier.height(4.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    PriceInfo(price = goods.price)
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
            name = "GTX 1060Ti",
            cover = "https://placekitten.com/200/287",
            price = BigDecimal.valueOf(99.99),
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