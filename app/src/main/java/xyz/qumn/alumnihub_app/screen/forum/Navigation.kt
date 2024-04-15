package xyz.qumn.alumnihub_app.screen.forum

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.forum(navController: NavController) {
    composable("/forum") {
        ForumScreen { id ->
            navController.navigate("/forum/post/$id?showBottom=false")
        }
    }
    composable("/forum/post/{id}?showBottom={showBottom}") { backStackEntity ->
        val id = backStackEntity.arguments?.getLong("id")
        PostDetailScreen(id) {
            navController.popBackStack()
        }
    }
    composable("/forum/post/add?showBottom={showBottom}") { backStackEntity ->
        val id = backStackEntity.arguments?.getLong("id")
        AddForumScreen {
            navController.popBackStack()
        }
    }
}
