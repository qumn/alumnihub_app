package xyz.qumn.alumnihub_app.composable

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import xyz.qumn.alumnihub_app.R

@Composable
fun Avatar(
    modifier: Modifier = Modifier.size(24.dp).clip(CircleShape),
    url: String,
) {
    AsyncImage(
        model = url,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        contentDescription = "seller avatar",
        placeholder = painterResource(id = R.drawable.placeholder),
        error = painterResource(id = R.drawable.placeholder),
    )
}
