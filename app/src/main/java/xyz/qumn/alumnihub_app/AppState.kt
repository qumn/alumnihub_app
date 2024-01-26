package xyz.qumn.alumnihub_app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

object AppState {
    internal val LocalSnackHostState: ProvidableCompositionLocal<SnackbarHostState> =
        staticCompositionLocalOf { error("no snack host be set") }

    internal val LocalNavController: ProvidableCompositionLocal<NavController> =
        staticCompositionLocalOf { error("no nav controller be set") }


    val navController: NavController
        @Composable get() = LocalNavController.current
}