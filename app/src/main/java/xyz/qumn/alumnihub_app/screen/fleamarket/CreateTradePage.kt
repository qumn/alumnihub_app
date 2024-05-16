package xyz.qumn.alumnihub_app.screen.fleamarket

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.AluSnackbarHost
import xyz.qumn.alumnihub_app.composable.CircularPulsatingIndicator
import xyz.qumn.alumnihub_app.composable.ImgGridPicker
import xyz.qumn.alumnihub_app.composable.SnackbarHelper
import xyz.qumn.alumnihub_app.composable.useSnack
import xyz.qumn.alumnihub_app.module.URL
import xyz.qumn.alumnihub_app.screen.fleamarket.module.GoodsApi
import xyz.qumn.alumnihub_app.ui.theme.Alumnihub_appTheme

@Composable
fun CreateTradePage(back: () -> Unit) {
    var price by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    val selectedImageUrl = remember { mutableStateListOf<URL>() }

    val priceRegex = Regex("^[0-9]+(\\.[0-9]{1,2})?$")
    val contentPlaceHolderTextStyle = MaterialTheme.typography.bodyMedium

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        floatingActionButton = { FloatingButton(desc, price, selectedImageUrl, back) },
        snackbarHost = { AluSnackbarHost() },
        bottomBar = {  }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = desc,
                onValueChange = { desc = it },
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
}

@Composable
private fun FloatingButton(
    desc: String,
    price: String,
    selectedImageUrl: SnapshotStateList<URL>,
    back: () -> Unit
) {
    val snackHelper = useSnack()
    var isLoading by remember { mutableStateOf(false) }
    FloatingActionButton(
        onClick = {
            if (isLoading) return@FloatingActionButton
            if (!validationInput(snackHelper, desc, price, selectedImageUrl)) {
                return@FloatingActionButton
            }
            val price = (price.toDoubleOrNull()!! * 100).toInt()
            isLoading = true
            CoroutineScope(Dispatchers.IO).launch {
                publishIdel(desc, price.toString(), selectedImageUrl).onSuccess {
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

private fun validationInput(
    snackHelper: SnackbarHelper,
    desc: String,
    price: String,
    imgs: List<URL>
): Boolean {
    if (desc.isBlank()) {
        snackHelper.show("请输入商品简介")
        return false
    } else if (price.isBlank() || price.toDoubleOrNull() == null || price.toDoubleOrNull()!! == 0.0) {
        snackHelper.show("请输入商品价格")
        return false
    } else if (imgs.isEmpty()) {
        snackHelper.show("请最少上传一张图片")
        return false
    }
    return true
}

suspend fun publishIdel(desc: String, price: String, imgs: List<URL>): Result<Long> {
    return GoodsApi.publish(desc, price, imgs)
}

@Composable
@Preview
fun AddForumScreenPreview() {
    Alumnihub_appTheme {
        CreateTradePage {}
    }
}
