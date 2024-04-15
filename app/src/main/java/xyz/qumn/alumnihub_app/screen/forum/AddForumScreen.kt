package xyz.qumn.alumnihub_app.screen.forum

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.qumn.alumnihub_app.composable.ImgGridPicker
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
import xyz.qumn.alumnihub_app.ui.theme.Blue60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddForumScreen(onClickBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val selectedImageUris = remember { mutableStateListOf<Uri>() }

    Scaffold(
        contentWindowInsets = WindowInsets(top = 0.dp),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onClickBack) {
                        Icon(Icons.Filled.ArrowBackIosNew, null)
                    }
                },
                title = {
                    Title("发布帖子")
                },
                actions = {
                    TextButton(
                        onClick = {},
                        modifier = Modifier.padding(2.dp, 0.dp),
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Blue60,
                            contentColor = Color.White
                        )
                    ) {
                        Text("发布")
                    }
                }
            )
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()

            val titlePlaceHolderTextStyle = MaterialTheme.typography.titleMedium
            val contentPlaceHolderTextStyle = MaterialTheme.typography.bodyMedium

            TextField(
                value = title,
                onValueChange = { title = it },
                suffix = {
                    if (isFocused)
                        Text(text = "${20 - title.length}")
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
                value = content,
                onValueChange = { content = it },
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
                        selectedImageUris,
                        onImgAdd = { selectedImages ->
                            Log.d("ImgGridPicker", "ImagePicker: call the on change function")
                            selectedImageUris.addAll(selectedImages)
                        },
                        onImgRemove = { idx ->
                            selectedImageUris.removeAt(idx)
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun AddForumScreenPreview() {
    Alumnihub_appTheme {
        AddForumScreen {}
    }
}
