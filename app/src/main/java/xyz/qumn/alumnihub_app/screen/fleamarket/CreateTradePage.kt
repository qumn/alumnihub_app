package xyz.qumn.alumnihub_app.screen.fleamarket

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.qumn.alumnihub_app.composable.ImgGridPicker
import xyz.qumn.alumnihub_app.module.URL
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme
import xyz.qumn.alumnihub_app.ui.theme.Blue60

@Composable
fun CreateTradePage() {
    var price by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val selectedImageUrl = remember { mutableStateListOf<URL>() }

    val priceRegex = Regex("^[0-9]+(\\.[0-9]{1,2})?$")
    val contentPlaceHolderTextStyle = MaterialTheme.typography.bodyMedium
    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "描述一下宝贝的品牌型号，货品来源...",
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

        Surface(Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxWidth()
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

        PriceInput(price) { newPrice ->
            if (newPrice.isEmpty() || priceRegex.matches(newPrice)) {
                price = newPrice
            }
        }

    }
}

@Composable
private fun PriceInput(price: String, onValueChange: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "价格",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black.copy(.6f)
        )
        TextField(
            value = price,
            onValueChange = onValueChange,
            prefix = {
                Text(
                    "¥",
                    Modifier.alignByBaseline(),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(255, 165, 0),
                )
            },
            placeholder = {
                Text(text = "0.0", color = Color(255, 165, 0))
            },
            modifier = Modifier.width(100.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(255, 165, 0),
                unfocusedTextColor = Color(255, 165, 0),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.White,
            )
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun topBar(onClickBack: () -> Unit, onClickPublish: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(Icons.Filled.ArrowBackIosNew, null)
            }
        },
        title = {
            Text(
                "闲置",
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            TextButton(
                onClick = onClickPublish,
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

suspend fun publishIdel(desc: String, price: String, imgs: List<URL>) {
    GoodsApi.publish(desc, price, imgs)
}

@Composable
@Preview
fun AddForumScreenPreview() {
    Alumnihub_appTheme {
        CreateTradePage()
    }
}
