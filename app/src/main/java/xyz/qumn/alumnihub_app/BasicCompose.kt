package xyz.qumn.alumnihub_app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import xyz.qumn.alumnihub_app.composable.Avatar
import xyz.qumn.alumnihub_app.screen.profile.LoginUser
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
import xyz.qumn.alumnihub_app.ui.theme.Blue50

@Composable
fun AluBottomBar(modifier: Modifier = Modifier) {
    val navController = AppState.LocalNavController.current
    val screens =
        listOf(Screen.FleaMarket, Screen.Forum, Screen.Add, Screen.LostFound, Screen.Profile)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Log.d("nav", "AluBottomBar: ${navBackStackEntry?.arguments}")
    val showBottom = !(navBackStackEntry?.arguments?.containsKey("showBottom")
        ?: false) // to show if not contains the showBottom  argument
            || navBackStackEntry?.arguments?.getBoolean("showBottom")!! // use the argument, if contains

    val to = { screen: Screen ->
        navController.navigate(screen.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    if (showBottom) {
        NavigationBar(
            modifier = modifier,
            containerColor = Color.White,
            windowInsets = NavigationBarDefaults.windowInsets
                .exclude(WindowInsets.navigationBars)
        ) {
            for (screen in screens) {
                NavigationBarItem(
                    selected = false,
                    onClick = { to(screen) },
                    icon = screen.icon,
                    enabled = screen.enable
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchApplicationBar(value: String, onValueChange: (String) -> Unit) {
    TopAppBar(
        modifier = Modifier.height(82.dp),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        title = {},
        actions = {
            Row(
                Modifier
                    .padding(24.dp, 2.dp, 24.dp, 6.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(Modifier.padding(end = 12.dp)) {
                    Avatar(
                        url = LoginUser().avatar,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(
                            RoundedCornerShape(28.dp)
                        )
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = value, onValueChange = onValueChange,
                        Modifier
                            .padding(start = 24.dp)
                            .weight(1f),
                        textStyle = TextStyle(fontSize = 15.sp)
                    ) {
                        if (value.isEmpty()) {
                            Text("搜搜看？", color = Color(0xffbfbfbf), fontSize = 15.sp)
                        }
                        it()
                    }
                    Box(
                        modifier = Modifier
                            .padding(6.dp)
                            .fillMaxHeight()
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(Blue50)
                    ) {
                        Icon(
                            Icons.Outlined.Search, contentDescription = "搜索",
                            Modifier
                                .size(24.dp)
                                .align(Alignment.Center),
                            tint = Color.White
                        )
                    }


                }
            }
        })
}


@Composable
fun AluSnackbarHost() {
    // preview model not have the local snack host
    SnackbarHost(AppState.LocalSnackHostState.current, Modifier.imePadding())
}


@Composable
@Preview
fun SearchApplicationBarPreview() {
    var searchText by remember { mutableStateOf("") }

    Alumnihub_appTheme {
        SearchApplicationBar(value = searchText) {
            searchText = it
        }
    }

}