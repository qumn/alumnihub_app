package xyz.qumn.alumnihub_app

import android.util.Log
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState

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

@Composable
fun AluSnackbarHost() {
    // preview model not have the local snack host
    SnackbarHost(AppState.LocalSnackHostState.current, Modifier.imePadding())
}