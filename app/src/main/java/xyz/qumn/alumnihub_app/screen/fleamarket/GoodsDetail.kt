package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.composable.SlidingCarousel
import xyz.qumn.alumnihub_app.module.User

@Composable
fun TradeDetailScreen(tradeId: Long?, onClickBack: () -> Unit) {
    val goods = GoodsApi.get(tradeId!!)

    val titleStyle = MaterialTheme.typography.titleLarge

    Scaffold(
        topBar = {
            TopBar(title = "商品详情", onClickBack)
        },
        bottomBar = {
            BottomBar()
        }
    ) {
        Column(Modifier.padding(it)) {
            Row(Modifier.padding(16.dp, 4.dp)) {
                SellerInfo(seller = goods.seller)
            }
            SlidingCarousel(
                images = goods.imgs,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )
            Text(goods.name, style = titleStyle)
            PriceInfo(
                price = goods.price, modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp, 8.dp)
            )
            Text(goods.desc, style = titleStyle)
        }
    }

}

@Composable
fun SellerInfo(seller: User) {
    Avatar(
        url = seller.avatar, modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
    )
    Spacer(Modifier.width(4.dp))
    Column(Modifier.padding(start = 8.dp), verticalArrangement = Arrangement.Top) {
        Text(seller.name, style = MaterialTheme.typography.titleMedium)
        Text(text = "3分钟回复", style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, onClickBack: () -> Unit) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(Icons.Filled.ArrowBackIosNew, null)
            }
        }
    )
}

@Composable
fun BottomBar() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Outlined.Chat, "leave a message")
            Text(text = "留言", style = MaterialTheme.typography.labelSmall)
        }
        Button(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "chat icon")
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "我想要", style = MaterialTheme.typography.titleSmall)
        }
    }
}


@Preview
@Composable
fun GoodsDetailPreview() {
    TradeDetailScreen(1L) {}
}