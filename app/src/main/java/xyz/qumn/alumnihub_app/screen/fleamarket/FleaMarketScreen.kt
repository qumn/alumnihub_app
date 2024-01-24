package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import xyz.qumn.alumnihub_app.composable.SlidingCarousel
import xyz.qumn.alumnihub_app.screen.fleamarket.module.Goods
import java.math.BigDecimal


@Composable
fun FleaMarketFlow() {
    val goods = GoodsApi.page(pageParam(10, 1))

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(goods) {
            FleaMarketCard(modifier = Modifier.defaultMinSize(minHeight = 360.dp), goods = it)
        }
    }
}

@Composable
fun FleaMarketCard(modifier: Modifier = Modifier, goods: Goods) {
    val titleStyle = MaterialTheme.typography.titleSmall

    Card(modifier.padding(3.dp)) {
        Column {
            SlidingCarousel(goods.imgs, modifier = Modifier.defaultMinSize(120.dp))
            Column(
                Modifier
                    .size(96.dp)
                    .padding(8.dp, 6.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = goods.name, style = titleStyle)
                PriceTag(price = goods.price)
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SellerAvatar(
                        avatarUrl = "https://placekitten.com/200/287",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                    )
                    SellerName(name = "Seller")
                }
            }
        }
    }
}

@Composable
fun PriceTag(
    modifier: Modifier = Modifier,
    price: BigDecimal,
    accentColor: Color = Color(255, 165, 0),
) {
    Row(modifier = modifier, verticalAlignment = Alignment.Bottom) {
        Text(
            text = "¥",
            style = MaterialTheme.typography.titleSmall,
            color = accentColor,
        )
        Text(
            text = price.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = accentColor
        )
    }
}

@Composable
fun SellerAvatar(avatarUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = avatarUrl,
        placeholder = painterResource(id = R.drawable.placeholder),
        contentScale = ContentScale.Crop,
        modifier = modifier,
        contentDescription = "seller avatar"
    )
}

@Composable
fun SellerName(name: String, modifier: Modifier = Modifier) {
    Text(text = name, style = MaterialTheme.typography.bodyMedium, modifier = modifier)
}


@Preview
@Composable
fun FleaMarketCardPreview() {
    FleaMarketCard(
        modifier = Modifier
            .width(200.dp)
            .height(300.dp)
            .padding(8.dp),
        goods = Goods(
            name = "GTX 1060Ti",
            imgs = listOf(
                "https://placekitten.com/200/287",
                "https://placekitten.com/201/287",
                "https://placekitten.com/202/287"
            ),
            price = BigDecimal.valueOf(99.99),
            sellerId = 1
        )
    )
}

@Preview
@Composable
fun FleaMarketFlowPreview() {
    FleaMarketFlow()
}