package xyz.qumn.alumnihub_app.screen.create

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.creation(navController: NavController) {
    composable("/creation?showBottom={showBottom}") {
        CreateScreen {
            navController.popBackStack()
        }
    }
}
