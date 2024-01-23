package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.qumn.alumnihub_app.composable.SlidingCarousel
import xyz.qumn.alumnihub_app.screen.fleamarket.module.Goods
import java.math.BigDecimal


@Composable
//@Preview(showBackground = true, backgroundColor = 0xffffff)
fun FleaMarket() {
    val numbers = (0..100).toList()

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(numbers) {
            Box(
                modifier = Modifier
                    .background(Color.Black)
                    .padding(8.dp)
                    .border(1.dp, Color.LightGray)
                    .height(82.dp),
            ) {
                Text(
                    text = "$it",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun FleaMarketCard(goods: Goods) {
    Card {
        Column {
            SlidingCarousel(goods.imgs)
            Text(text = goods.name)
        }
    }
}


@Preview
@Composable
fun fleaMarketCardPreview() {
    val goods = Goods(
        name = "GTX 1060Ti",
        imgs = listOf(
            "https://placekitten.com/402/287",
            "https://placekitten.com/201/287",
            "https://placekitten.com/202/287"
        ),
        price = BigDecimal.valueOf(99.99),
        sellerId = 1
    )

    FleaMarketCard(goods)
}