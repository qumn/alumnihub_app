package xyz.qumn.alumnihub_app.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import xyz.qumn.alumnihub_app.R

@Composable
fun ImgGrid(imgs: List<String>, cols: Int = 3, rows: Int = 3) {
    if (imgs.isEmpty()) return

    val availableImgs = imgs.take(cols * rows)
    LazyVerticalGrid(columns = GridCells.Fixed(cols)) {
        val modifier = Modifier.padding(2.dp)
        items(availableImgs) { imgUrl ->
            AsyncImage(
                model = imgUrl,
                modifier = modifier,
                placeholder = painterResource(id = R.drawable.placeholder),
                contentDescription = "image"
            )
        }
    }
}