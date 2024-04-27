package xyz.qumn.alumnihub_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.outlined.Architecture
import androidx.compose.material.icons.outlined.Cottage
import androidx.compose.material.icons.outlined.CurrencyExchange
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.FlagCircle
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material.icons.outlined.LocationSearching
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.OutlinedFlag
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import xyz.qumn.alumnihub_app.ui.theme.Blue50

sealed class Screen(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
    val isShowBottomBar: Boolean = false,
    val enable: Boolean = true
) {
    object FleaMarket :
        Screen(
            "/flea_market",
            "跳蚤市场",
            { Icon(Icons.Outlined.ShoppingBasket, "跳蚤市场") },
            true
        )

    object Forum :
        Screen(
            "/forum",
            "论坛",
            { Icon(Icons.Outlined.Forum, "论坛") },
            true
        )

    object Add :
        Screen(
            "/creation?showBottom=false",
            "发帖",
            {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Blue50)
                ) {
                    Icon(
                        Icons.Filled.Add,
                        "发帖",
                        modifier = Modifier.padding(4.dp),
                        tint = Color.White
                    )
                }
            },
            true
        )

    object LostFound :
        Screen(
            "/lost_found",
            "失物招领",
            { Icon(Icons.Outlined.ImageSearch, "失物招领") },
            true,
            true,
        )

    object Profile :
        Screen(
            "/profile",
            "个人中心",
            { Icon(Icons.Outlined.Person, "个人中心") },
            true,
            true
        )
}
