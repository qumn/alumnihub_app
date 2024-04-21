package xyz.qumn.alumnihub_app.screen.forum

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavGraphBuilder.forum(navController: NavController) {
    composable("/forum") {
        ForumScreen { id ->
            navController.navigate(
                "/forum/post/$id?showBottom=false",
            )
        }
    }
    composable(
        "/forum/post/{id}?showBottom={showBottom}",
        arguments = listOf(navArgument("id") { type = NavType.LongType })
    ) { backStackEntity ->
        val id = backStackEntity.arguments?.getLong("id")
        PostDetailScreen(id) {
            navController.popBackStack()
        }
    }
}
