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
import java.math.BigDecimal

@Composable
fun PriceInfo(
    modifier: Modifier = Modifier,
    price: BigDecimal,
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
            price.toString(),
            Modifier.alignByBaseline(),
            style = MaterialTheme.typography.titleLarge,
            color = accentColor
        )
    }
}
