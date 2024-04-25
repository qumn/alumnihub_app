package xyz.qumn.alumnihub_app.screen.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.login(navController: NavController) {
    composable("/login?showBottom={showBottom}") {
        LoginPage {
            navController.navigate("/flea_market")
        }
    }
}
