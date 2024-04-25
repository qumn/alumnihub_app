package xyz.qumn.alumnihub_app.screen.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import xyz.qumn.alumnihub_app.screen.lostfound.LostFoundScreen

fun NavGraphBuilder.profile(navController: NavController) {
    composable("/profile") {
        ProfileScreen()
    }
}
