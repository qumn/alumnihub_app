package xyz.qumn.alumnihub_app

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.runBlocking
import xyz.qumn.alumnihub_app.api.token
import xyz.qumn.alumnihub_app.composable.useSnack
import xyz.qumn.alumnihub_app.data.TokenManager
import xyz.qumn.alumnihub_app.screen.create.creation
import xyz.qumn.alumnihub_app.screen.fleamarket.fleaMarket
import xyz.qumn.alumnihub_app.screen.forum.forum
import xyz.qumn.alumnihub_app.screen.login.login
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        super.onCreate(savedInstanceState)

        setContent {
//            TransparentSystemBars()
            Alumnihub_appTheme {
                AlumnihubApp()
            }
        }
    }
}

@Composable
fun TransparentSystemBars() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )

        onDispose {}
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlumnihubApp() {
    val navController = rememberNavController()
    val snackbarHostState = SnackbarHostState()
    // get the context
    val context = LocalContext.current
    runBlocking {
        token = TokenManager.getToken(context)
    }

    CompositionLocalProvider(
        AppState.LocalSnackHostState provides snackbarHostState,
        AppState.LocalNavController provides navController
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState, Modifier.imePadding()) },
            bottomBar = { AluBottomBar(navController) },
            contentWindowInsets = ScaffoldDefaults
                .contentWindowInsets
                .exclude(WindowInsets.statusBars)
                .exclude(WindowInsets.ime)
        ) {
            Column(Modifier.padding(it)) {
                NavHost(
                    navController = navController,
                    startDestination = if (token == null) "/login" else Screen.FleaMarket.route,
                    enterTransition = {
                        slideInHorizontally(
                            animationSpec = tween(300),
                            initialOffsetX = { fullWidth -> fullWidth })
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            animationSpec = tween(300),
                            targetOffsetX = { fullWidth -> 2 * fullWidth })
                    }
                ) {
                    login(navController)
                    fleaMarket(navController)
                    forum(navController)
                    creation(navController)
                }
            }
        }
    }
}

@Composable
private fun AluBottomBar(navController: NavHostController) {
    val screens =
        listOf(Screen.FleaMarket, Screen.Forum, Screen.Add, Screen.LostFound, Screen.Profile)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Log.d("nav", "AluBottomBar: ${navBackStackEntry?.arguments}")
    val showBottom = !(navBackStackEntry?.arguments?.containsKey("showBottom")
        ?: false) // to show if not contains the showBottom  argument
            || navBackStackEntry?.arguments?.getBoolean("showBottom")!! // use the argument, if contains

    val to = { screen: Screen ->
        navController.navigate(screen.route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
    if (showBottom) {
        NavigationBar(containerColor = Color.White) {
            for (screen in screens) {
                NavigationBarItem(
                    selected = false,
                    onClick = { to(screen) },
                    icon = screen.icon
                )
            }
        }
    }
}

@Composable
fun Profile() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            "Home",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )

        val snackHelper = useSnack()

        Button(onClick = {
            snackHelper.show("Hello Home")
        }) {
            Text(text = "Show message")
        }
    }
}


@Composable
fun LostFound() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            "Lost Found",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
        val snackHelper = useSnack()

        Button(onClick = {
            snackHelper.show("Hello Lost Found")
        }) {
            Text(text = "Show message")
        }
    }
}