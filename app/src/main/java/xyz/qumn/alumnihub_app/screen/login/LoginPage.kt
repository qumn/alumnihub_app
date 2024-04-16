package xyz.qumn.alumnihub_app.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.api.UserApi
import xyz.qumn.alumnihub_app.api.token
import xyz.qumn.alumnihub_app.data.TokenManager

@Composable
fun LoginPage(back: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold {
        Column(Modifier.padding(it)) {
            TextField(value = username, onValueChange = { username = it })
            TextField(value = password, onValueChange = { password = it })
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    token = UserApi.login(username, password)
                    TokenManager.saveToken(context, token!!)
                    CoroutineScope(Dispatchers.Main).launch {
                        back()
                    }
                }
            }) {
                Text("Login")
            }
        }
    }
}