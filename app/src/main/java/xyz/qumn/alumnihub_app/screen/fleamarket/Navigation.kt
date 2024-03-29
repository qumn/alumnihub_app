package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.fleaMarket(navController: NavController) {
    composable("/flea_market") {
        FleaMarketFlowScreen { id ->
            navController.navigate("/flea_market/trade/$id")
        }
    }
    composable("/flea_market/trade/{id}") {backStackEntiy ->
        val id = backStackEntiy.arguments?.getLong("id")
        TradeDetailScreen(id) {
            navController.popBackStack()
        }
    }
}