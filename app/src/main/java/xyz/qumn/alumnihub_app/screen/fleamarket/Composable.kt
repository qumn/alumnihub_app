package xyz.qumn.alumnihub_app.screen.fleamarket

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PriceInfo(
    modifier: Modifier = Modifier,
    priceInCent: Int,
    accentColor: Color = Color(255, 165, 0),
) {
    Row(modifier = modifier) {
        Text(
            "¥",
            Modifier.alignByBaseline(),
            style = MaterialTheme.typography.titleMedium,
            color = accentColor,
        )
        Spacer(modifier = Modifier.width(1.dp))
        Text(
            convertPriceToYuan(priceInCent),
            Modifier.alignByBaseline(),
            style = MaterialTheme.typography.titleLarge,
            color = accentColor
        )
    }
}


fun convertPriceToYuan(priceInCent: Int): String {
    val yuan = priceInCent / 100 // 分转换为元
    val fen = priceInCent % 100 // 余下的分

    // 格式化字符串，保留两位小数
    val formattedFen = if (fen < 10) "0$fen" else fen.toString()

    return "$yuan.$formattedFen 元"
}