package xyz.qumn.alumnihub_app.screen.lostfound

import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.outlined.QuestionAnswer
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
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
import xyz.qumn.alumnihub_app.module.URL
import xyz.qumn.alumnihub_app.screen.lostfound.module.LostItem
import xyz.qumn.alumnihub_app.screen.lostfound.module.LostItemApi
import xyz.qumn.alumnihub_app.screen.lostfound.module.PublishMissingItemReq
import xyz.qumn.alumnihub_app.screen.lostfound.module.Question
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme

@Composable
fun PublishMissingItemPage(onClickBack: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    val titlePlaceHolderTextStyle = MaterialTheme.typography.titleMedium
    val selectedImageUrl = remember { mutableStateListOf<URL>() }
    var publishMissingItemReq by remember { mutableStateOf(PublishMissingItemReq.empty()) }
    var newQuestion by remember {
        mutableStateOf(Question.empty())
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = {
            FloatingButton(
                publishMissingItemReq.copy(imgs = selectedImageUrl.map { it.urn.name }),
                onClickBack
            )
        },
        snackbarHost = { AluSnackbarHost() },
        bottomBar = { }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "基本信息", style = MaterialTheme.typography.titleLarge, modifier = Modifier
                    .align(Alignment.Start)
                    .padding(12.dp)
            )
            Surface(modifier = Modifier.padding(12.dp)) {
                Column {

                    TextField(
                        value = publishMissingItemReq.name,
                        onValueChange = {
                            publishMissingItemReq = publishMissingItemReq.copy(name = it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "输入物品名称",
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
                        value = publishMissingItemReq.location,
                        onValueChange = {
                            publishMissingItemReq = publishMissingItemReq.copy(location = it)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        prefix = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                        placeholder = {
                            Text(
                                "物品拾取位置",
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
                }
            }


            val tryAddQuestion = {
                if (newQuestion.content.isNotEmpty() && newQuestion.answer.isNotEmpty()) {
                    publishMissingItemReq =
                        publishMissingItemReq.copy(questions = publishMissingItemReq.questions + newQuestion)
                    newQuestion = Question.empty()
                }
            }

            Text(
                "问题", style = MaterialTheme.typography.titleLarge, modifier = Modifier
                    .align(Alignment.Start)
                    .padding(12.dp)
            )
            Surface(Modifier.padding(12.dp)) {
                Column {
                    for (question in publishMissingItemReq.questions) {
                        Column(Modifier.padding(0.dp, 3.dp)) {
                            Row(Modifier.fillMaxWidth()) {
                                Icon(Icons.Outlined.QuestionMark, contentDescription = null)
                                Text(text = question.content)
                            }
                            Row(Modifier.fillMaxWidth()) {
                                Icon(Icons.Outlined.QuestionAnswer, contentDescription = null)
                                Text(text = question.answer)
                            }
                        }
                    }
                    TextField(
                        value = newQuestion.content,
                        modifier = Modifier.fillMaxWidth(),
                        onValueChange = { newQuestion = newQuestion.copy(content = it) },
                        placeholder = {
                            Text(
                                "问题描述",
                                style = titlePlaceHolderTextStyle,
                                color = Color.Black.copy(.6f)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    TextField(
                        value = newQuestion.answer,
                        placeholder = {
                            Text(
                                "问题答案",
                                style = titlePlaceHolderTextStyle,
                                color = Color.Black.copy(.6f)
                            )
                        },
                        onValueChange = { newQuestion = newQuestion.copy(answer = it) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { tryAddQuestion() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (!it.isFocused) tryAddQuestion()
                            },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }
            }

            Surface(Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImgGridPicker(
                        selectedImageUrl,
                        onImgAdd = { selectedImages ->
                            Log.d("ImgGridPicker", "ImagePicker: call the on change function")
                            selectedImageUrl.addAll(selectedImages)
                        },
                        onImgRemove = { idx ->
                            selectedImageUrl.removeAt(idx)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingButton(
    publishMissingItemReq: PublishMissingItemReq,
    back: () -> Unit
) {
    val snackHelper = useSnack()
    var isLoading by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = {
            if (isLoading) return@FloatingActionButton
            val msg = publishMissingItemReq.validate()
            if (msg != null) {
                snackHelper.show(msg)
                return@FloatingActionButton
            }
            isLoading = true
            CoroutineScope(Dispatchers.IO).launch {
                LostItemApi.publishMissingItem(publishMissingItemReq).onSuccess {
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



@Preview
@Composable
fun PublishMissingItemPreview() {
    AppState.ProvideAppState {
        Alumnihub_appTheme {
            PublishMissingItemPage {}
        }
    }
}
