package xyz.qumn.alumnihub_app.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.AppState
import xyz.qumn.alumnihub_app.api.UserApi
import xyz.qumn.alumnihub_app.composable.useSnack
import xyz.qumn.alumnihub_app.data.TokenManager

enum class LoginType {
    PASSWORD, CODE
}

@Composable
fun LoginPage(back: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val snackHelper = useSnack()
    val titleStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
    val tipsStyle = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Thin)

    Scaffold {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Text(text = "账号密码登录", style = titleStyle)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "未注册的手机号登录成功后将自动注册", style = tipsStyle)

            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "手机号") },
                prefix = { Text(text = "+86") }
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "密码") })
            Spacer(modifier = Modifier.height(30.dp))
            Button(modifier = Modifier
                .width(200.dp)
                .height(60.dp), onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    UserApi.login(username, password)
                        .onSuccess { token ->
                            TokenManager.saveToken(context, token)
                            CoroutineScope(Dispatchers.Main).launch {
                                back()
                            }
                        }
                        .onFailure {
                            snackHelper.show("账号密码错误")
                        }
                }
            }) {
                Text("登录")
            }
        }
    }
}

@Composable
@Preview
fun LoginPagePreview() {
    AppState.ProvideAppState {
        LoginPage {

        }
    }
}