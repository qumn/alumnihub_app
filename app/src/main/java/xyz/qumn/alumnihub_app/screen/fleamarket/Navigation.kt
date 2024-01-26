package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.FleaMarket() {
    composable("/fleamarket") {
        FleaMarketFlow()
    }
    composable("/fleamarket/trade/{id}") {backStackEntiy ->
        val id = backStackEntiy.arguments?.getLong("id")
        TradeDetailScreen(id)
    }
}