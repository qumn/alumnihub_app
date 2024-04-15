package xyz.qumn.alumnihub_app.screen.forum

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme

@Composable
fun AddForumScreen() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopBar(title = "发布帖子") {
            }
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
                        "输入标题会跟受欢迎",
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
        }
    }
}

@Composable
@Preview
fun AddForumScreenPreview() {
    Alumnihub_appTheme {
        AddForumScreen()
    }
}
