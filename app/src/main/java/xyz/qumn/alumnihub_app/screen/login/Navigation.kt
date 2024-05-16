package xyz.qumn.alumnihub_app.screen.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import xyz.qumn.alumnihub_app.LoadLoginUserInfo

fun NavGraphBuilder.login(navController: NavController) {
    composable("/login?showBottom={showBottom}") {
        LoginPage {
            navController.navigate("/flea_market")
        }
    }
}
