package xyz.qumn.alumnihub_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Alumnihub_appTheme {
                // A surface container using the 'background' color from the theme
//                Greeting("Android")
                AlumnihubApp()
            }
        }
    }
}

sealed class Screen(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val isShowBottomBar: Boolean = false
) {
    object FleaMarket : Screen("/flea_market", "跳蚤市场", Icons.Filled.ShoppingCart, true)
    object LostFound : Screen("/lost_found", "失物招领", Icons.Filled.Info, true)
    object Profile : Screen("/profile", "个人中心", Icons.Filled.Person, true)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun AlumnihubApp() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { AppBar() }
    ) {
        NavHost(navController = navController, startDestination = Screen.FleaMarket.route) {
            composable(Screen.Profile.route) { Profile() }
            composable(Screen.FleaMarket.route) { FleaMarket() }
            composable(Screen.LostFound.route) { LostFound() }
        }
    }


}

@Composable
private fun AppBar() {
    val screens = listOf(Screen.FleaMarket, Screen.LostFound, Screen.Profile)

    NavigationBar {
        for (screen in screens) {
            NavigationBarItem(
                selected = false,
                onClick = { println("click first") },
                icon = { Icon(screen.icon, null) },
            )
        }
    }
}

@Composable
fun Profile() {
    Text("Home")
}

@Composable
fun FleaMarket() {
    Text("flea market")
}

@Composable
fun LostFound() {
    Text("Lost Found")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val tasks = remember { mutableStateListOf<String>() }
    var task by remember { mutableStateOf("") }

    Alumnihub_appTheme {
        Surface(color = Color.Cyan) {
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Todo List",
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    OutlinedTextField(
                        value = task,
                        modifier = Modifier
                            .weight(0.6f)
                            .fillMaxWidth(),
                        onValueChange = {
                            task = it
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        onClick = {
                            tasks.add(task)
                            task = ""
                        }) {
                        Text(text = "Add", softWrap = false)
                    }
                }
                for ((i, t) in tasks.withIndex()) {
                    Text(text = "$i. $t", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}