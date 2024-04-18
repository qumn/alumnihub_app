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
    val isShowBottomBar: Boolean = false
) {
    object FleaMarket :
        Screen(
            "/flea_market",
            "跳蚤市场",
            { Icon(Icons.Filled.Store, "跳蚤市场") },
            true
        )

    object Forum :
        Screen(
            "/forum",
            "论坛",
            { Icon(Icons.Filled.Forum, "论坛") },
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
            false
        )

    object LostFound :
        Screen(
            "/lostfound",
            "失物招领",
            { Icon(Icons.Filled.Flag, "失物招领") },
            true
        )

    object Profile :
        Screen(
            "/profile",
            "个人中心",
            { Icon(Icons.Filled.Person, "个人中心") },
            true
        )
}
