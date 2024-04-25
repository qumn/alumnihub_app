package xyz.qumn.alumnihub_app.screen.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.qumn.alumnihub_app.AluSnackbarHost
import xyz.qumn.alumnihub_app.AppState
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.module.Gender
import xyz.qumn.alumnihub_app.module.User
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
import xyz.qumn.alumnihub_app.ui.theme.Gray50
import xyz.qumn.alumnihub_app.ui.theme.Gray60

@Composable
fun ProfileScreen() {
    val user = LoginUser()
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
                .padding(12.dp)
        ) {
            Box(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(
                        url = user.avatar, modifier = Modifier
                            .padding(12.dp)
                            .size(82.dp)
                    )
                    Text(text = user.name, style = MaterialTheme.typography.titleLarge)
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp, 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "查看我的主页",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Thin,
                            color = Gray60.copy(alpha = .8f)
                        )
                    )
                    Icon(
                        Icons.AutoMirrored.Outlined.NavigateNext,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = Gray60.copy(alpha = .8f)
                    )
                }
            }
            Surface {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(82.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "12",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(text = "商品", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(
                        Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "123",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(text = "帖子", style = MaterialTheme.typography.labelSmall)
                    }
                    Column(
                        Modifier.fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "15",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(text = "失物", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val list =
                listOf(
                    Icons.Filled.Edit to "我的帖子",
                    Icons.Filled.ShoppingBasket to "我的商品",
                    Icons.Filled.Flag to "我的拾取物",
                    Icons.Filled.ThumbUp to "我赞过的内容",
                    Icons.Filled.Star to "我的收藏",
                    Icons.Filled.QuestionMark to "联系开发者",
                    Icons.Filled.Settings to "设置",
                )
            LazyColumn {
                items(list) { (icon, text) ->
                    Row(
                        Modifier
                            .padding(12.dp, 8.dp)
                            .height(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = icon, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = text)
                    }
                    HorizontalDivider(
                        modifier = Modifier.padding(20.dp, 0.dp),
                        thickness = 1.dp,
                        color = Gray50.copy(alpha = .6f)
                    )
                }
            }

        }
    }

}

fun LoginUser(): User =
    User(
        1L,
        "张三",
        avatar = "https://picsum.photos/201/300",
        Gender.UNKNOWN,
        null,
        "",
        ""
    )

@Preview
@Composable
fun ProfileScreenPreview() {
    AppState.ProvideAppState {
        Alumnihub_appTheme {
            ProfileScreen(
            )
        }
    }
}