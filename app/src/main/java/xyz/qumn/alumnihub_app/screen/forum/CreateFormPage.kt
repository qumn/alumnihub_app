package xyz.qumn.alumnihub_app.screen.forum

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.AluSnackbarHost
import xyz.qumn.alumnihub_app.AppState
import xyz.qumn.alumnihub_app.composable.CircularPulsatingIndicator
import xyz.qumn.alumnihub_app.composable.ImgGridPicker
import xyz.qumn.alumnihub_app.composable.useSnack
import xyz.qumn.alumnihub_app.screen.forum.module.PostApi
import xyz.qumn.alumnihub_app.screen.forum.module.PostCreateReq
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme


@Composable
fun CreateFormPage(back: () -> Unit) {
    var postCreateReq by remember { mutableStateOf(PostCreateReq.empty()) }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = { FloatingButton(postCreateReq, back) },
        snackbarHost = { AluSnackbarHost() },
        bottomBar = { }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()

            val titlePlaceHolderTextStyle = MaterialTheme.typography.titleMedium
            val contentPlaceHolderTextStyle = MaterialTheme.typography.bodyMedium

            TextField(
                value = postCreateReq.title,
                onValueChange = { postCreateReq = postCreateReq.copy(title = it) },
                suffix = {
                    if (isFocused)
                        Text(text = "${20 - postCreateReq.title.length}")
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "输入标题会更受欢迎",
                        style = titlePlaceHolderTextStyle,
                        color = Color.Black.copy(.6f)
                    )
                },
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White
                )
            )
            TextField(
                value = postCreateReq.content,
                onValueChange = { postCreateReq = postCreateReq.copy(content = it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "分享此刻想法",
                        style = contentPlaceHolderTextStyle,
                        color = Color.Black.copy(.6f)
                    )
                },
                minLines = 3,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                )
            )

            Surface(Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImgGridPicker(
                        postCreateReq.imgs,
                        onImgAdd = { selectedImages ->
                            Log.d("ImgGridPicker", "ImagePicker: call the on change function")
                            postCreateReq =
                                postCreateReq.copy(imgs = postCreateReq.imgs + selectedImages)
                        },
                        onImgRemove = { idx ->
                            postCreateReq =
                                postCreateReq.copy(imgs = postCreateReq.imgs.filterIndexed { i, _ -> idx != i })
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingButton(
    postCreateReq: PostCreateReq,
    back: () -> Unit
) {
    val snackHelper = useSnack()
    var isLoading by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = {
            if (isLoading) return@FloatingActionButton
            val msg = postCreateReq.validate()
            if (msg != null) {
                snackHelper.show(msg)
                return@FloatingActionButton
            }
            isLoading = true
            CoroutineScope(Dispatchers.IO).launch {
                PostApi.createNewPost(postCreateReq).onSuccess {
                    CoroutineScope(Dispatchers.Main).launch {
                        isLoading = false
                        snackHelper.show("发布成功")
                        back()
                    }
                }.onFailure {
                    CoroutineScope(Dispatchers.Main).launch {
                        isLoading = false
                        snackHelper.show("网络波动, 请稍候再试")
                    }
                }
            }
        },
        Modifier
            .imePadding()
    ) {
        if (isLoading) {
            CircularPulsatingIndicator()
        } else {
            Icon(Icons.Filled.Navigation, null)
        }
    }
}

@Composable
@Preview
fun AddForumScreenPreview() {
    AppState.ProvideAppState {
        Alumnihub_appTheme {
            CreateFormPage {}
        }
    }
}
