package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import xyz.qumn.alumnihub_app.R
import xyz.qumn.alumnihub_app.screen.fleamarket.module.Goods
import java.math.BigDecimal


@Composable
fun FleaMarketFlow() {
    val goods = GoodsApi.page(pageParam(10, 1))

    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
        items(goods.size) {
            FleaMarketCard(
                goods = goods[it]
            )
        }
    }
}

@Composable
fun FleaMarketCard(modifier: Modifier = Modifier, goods: Goods) {
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
                    .fillMaxWidth(),
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
                ) {
                    PriceInfo(price = goods.price)
                    SellerInfo(goods.sellerAvatar, goods.sellerName)
                }
            }
        }
    }
}

@Composable
fun PriceInfo(
    modifier: Modifier = Modifier,
    price: BigDecimal,
    accentColor: Color = Color(255, 165, 0),
) {
    Row(modifier = modifier) {
        Text(
            "¥",
            Modifier.alignByBaseline(),
            style = MaterialTheme.typography.titleMedium,
            color = accentColor,
        )
        Spacer(modifier = Modifier.width(1.dp))
        Text(
            price.toString(),
            Modifier.alignByBaseline(),
            style = MaterialTheme.typography.titleLarge,
            color = accentColor
        )
    }
}

@Composable
private fun SellerInfo(avatar: String, name: String) {
    Row(verticalAlignment = Alignment.Bottom) {
        SellerAvatar(
            avatarUrl = avatar,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SellerAvatar(avatarUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = avatarUrl,
        modifier = modifier,
        placeholder = painterResource(id = R.drawable.placeholder),
        contentScale = ContentScale.Crop,
        contentDescription = "seller avatar"
    )
}


@Preview
@Composable
fun FleaMarketCardPreview() {
    FleaMarketCard(
        modifier = Modifier
            .width(240.dp)
            .padding(8.dp),
        goods = Goods(
            name = "GTX 1060Ti",
            cover = "https://placekitten.com/200/287",
            price = BigDecimal.valueOf(99.99),
            sellerId = 1,
            sellerAvatar = "https://placekitten.com/201/287",
            sellerName = "张三"
        )
    )
}

@Preview
@Composable
fun FleaMarketFlowPreview() {
    FleaMarketFlow()
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