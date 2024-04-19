package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import xyz.qumn.alumnihub_app.screen.conversation.ConversationContent
import xyz.qumn.alumnihub_app.screen.conversation.exampleUiState


fun NavGraphBuilder.fleaMarket(navController: NavController) {
    composable("/flea_market") {
        FleaMarketFlowScreen { id ->
            navController.navigate("/flea_market/trade/$id?showBottom=false")
        }
    }
    composable(
        "/flea_market/trade/{id}?showBottom={showBottom}"
    ) { backStackEntity ->
        val id = backStackEntity.arguments?.getLong("id")
        TradeDetailScreen(id, onClickBack = {
            navController.popBackStack()
        }, toConversation = {
            navController.navigate("/flea_market/trade/conversation/$id?showBottom=false")
        })
    }
    composable(
        "/flea_market/trade/conversation/{id}?showBottom={showBottom}"
    ) { backStackEntity ->
        val id = backStackEntity.arguments?.getLong("id")
        ConversationContent(
            uiState = exampleUiState,
            navigateToProfile = { }
        )
    }
}