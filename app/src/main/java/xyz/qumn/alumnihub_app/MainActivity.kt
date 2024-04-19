package xyz.qumn.alumnihub_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.runBlocking
import xyz.qumn.alumnihub_app.api.LoginUser
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
            TransparentSystemBars()
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

        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
        )

        onDispose {}
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AlumnihubApp() {
    LoadLoginUserInfo()

    AppState.ProvideAppState {
        val navController = AppState.navController
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            NavHost(
                modifier = Modifier.weight(0.91f),
                navController = navController,
                startDestination = if (LoginUser.token == null) "/login" else Screen.FleaMarket.route,
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
            AluBottomBar(Modifier.weight(0.09f))
        }
    }
}

@Composable
fun LoadLoginUserInfo() {
    val context = LocalContext.current
    runBlocking {
        LoginUser.token = TokenManager.getToken(context)
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