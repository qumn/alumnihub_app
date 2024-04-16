package xyz.qumn.alumnihub_app.composable

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.woong.compose.grid.SimpleGridCells
import io.woong.compose.grid.VerticalGrid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import xyz.qumn.alumnihub_app.R
import xyz.qumn.alumnihub_app.api.ApiClient

@Composable
fun ImgGridPicker(
    imgs: Collection<String?>,
    onImgRemove: (Int) -> Unit,
    onImgAdd: (List<String>) -> Unit,
    height: Dp = 120.dp,
    maxCols: Int = 3
) {
    Log.d("image-grid-picker", "ImgGridPicker: ${imgs}")
    VerticalGrid(columns = SimpleGridCells.Fixed(maxCols), Modifier.fillMaxWidth()) {
        for ((idx, img) in imgs.withIndex()) {
            Box {
                AsyncImage(
                    model = img,
                    modifier = Modifier
                        .padding(2.dp)
                        .height(height),
                    contentDescription = "image",
                    placeholder = painterResource(id = R.drawable.placeholder),
                    error = painterResource(id = R.drawable.placeholder),
                )
                Surface(
                    modifier = Modifier
                        .padding(top = 6.dp, end = 12.dp)
                        .align(Alignment.TopEnd)
                        .clickable { onImgRemove(idx) },
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.5f)

                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "remove the image",
                        tint = Color.White
                    )
                }
            }
        }
        if (imgs.size < 9) {
            Log.d("ImgGridPicker", "ImgGridPicker image size: ${imgs.size}")
            ImageAdd(onImgAdd, height = height, maxItems = 9 - imgs.size)
        }
    }
}


@Composable
private fun ImageAdd(
    onImageAdd: (List<String>) -> Unit,
    maxItems: Int = 9,
    height: Dp = 120.dp,
    width: Dp = 20.dp
) {

    val contentResolver = LocalContext.current.contentResolver
    val photoPickerLauncher = if (maxItems == 1) {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { it ->
                it?.let { uri ->
                    uri ?: return@let
                    CoroutineScope(Dispatchers.IO).launch {
                        val urls = uploadFiles(contentResolver, listOf(uri))
                        CoroutineScope(Dispatchers.Main).launch {
                            onImageAdd(urls)
                        }
                    }
                }
            }
        )
    } else {
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems),
            onResult = { uris ->
                CoroutineScope(Dispatchers.IO).launch {
                    val urls = uploadFiles(contentResolver, uris)
                    CoroutineScope(Dispatchers.Main).launch {
                        onImageAdd(urls)
                    }
                }
            }
        )
    }
    IconButton(
        onClick = {
            photoPickerLauncher.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        },
        modifier = Modifier
            .width(width)
            .height(height)
            .background(
                Color.Gray.copy(.2f)
            )
    ) {
        Icon(
            Icons.Outlined.Add,
            contentDescription = "add a image",
            Modifier
                .size(36.dp),
            tint = Color.White,
        )
    }
}


suspend fun uploadFiles(contentResolver: ContentResolver, uris: List<Uri>): List<String> =
    coroutineScope {
        uris.map { uri ->
            async { ApiClient.upload(contentResolver, uri) }
        }
            .map {
                it.await()
            }
            .filterNotNull()
            .map { it }
    }

@Composable
@Preview
fun ImgGridPickerPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        ImgGridPicker(listOf(), {}, {})
    }
}