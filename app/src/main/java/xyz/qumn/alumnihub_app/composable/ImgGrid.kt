package xyz.qumn.alumnihub_app.composable

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.woong.compose.grid.SimpleGridCells
import io.woong.compose.grid.VerticalGrid
import xyz.qumn.alumnihub_app.R

@Composable
fun ImgGrid(imgs: Collection<Any?>, maxCols: Int = 3) {
    if (imgs.isEmpty()) return
    val cols = calculateMaxCols(imgs.size)
    VerticalGrid(columns = SimpleGridCells.Fixed(cols), Modifier.fillMaxWidth()) {
        for (img in imgs) {
            Log.i("ImgGrid", "ImgGrid: $img")
            val modifier = Modifier.padding(2.dp)
            AsyncImage(
                model = img,
                modifier = modifier,
                contentDescription = "image",
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder),
            )
        }
    }
}

private fun calculateMaxCols(size: Int): Int {
    if (size <= 2) return size;

    return if (size <= 4) 2
    else 3
}