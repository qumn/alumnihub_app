package xyz.qumn.alumnihub_app

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object AppState {
    internal val LocalSnackHostState: ProvidableCompositionLocal<SnackbarHostState> =
        staticCompositionLocalOf { error("no local snack host be set") }

    internal val LocalNavController: ProvidableCompositionLocal<NavHostController> =
        staticCompositionLocalOf { error("no nav controller be set") }

    internal val LocalContext: ProvidableCompositionLocal<Context> =
        staticCompositionLocalOf { error("no nav controller be set") }



    val navController: NavHostController
        @Composable get() = LocalNavController.current

    @Composable
    fun ProvideAppState(content: @Composable () -> Unit) {
        val navController = rememberNavController()
        val snackbarHostState = SnackbarHostState()

        CompositionLocalProvider(
            LocalSnackHostState provides snackbarHostState,
            LocalNavController provides navController
        ) {
            content()
        }
    }
}