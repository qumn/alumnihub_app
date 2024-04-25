package xyz.qumn.alumnihub_app.screen.lostfound

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.lostfound(navController: NavController) {
    composable("/lost_found") {
        LostFoundScreen()
    }
}
