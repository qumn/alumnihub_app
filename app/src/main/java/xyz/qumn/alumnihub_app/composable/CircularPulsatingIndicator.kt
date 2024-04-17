package xyz.qumn.alumnihub_app.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularPulsatingIndicator(
    color: Color = Color.White,
    animationDuration: Int = 850,
    progress: Float = 0.8f, // must be less than 1.0
    canvasSize: Float = 50f,
    penThickness: Dp = 2.dp,
) {

    val transition = rememberInfiniteTransition()

    // Turning Around Animation
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(modifier = Modifier) {

        val sweepAngle = if (progress > 1) 360f else (360f * progress)
        val topArcStartAngle = 0f - rotation // Starting angle in degrees

        val arcPath = Path().apply {
            addArc(
                oval = Rect(
                    left = -canvasSize,
                    top = -canvasSize,
                    right = canvasSize,
                    bottom = canvasSize
                ),
                startAngleDegrees = topArcStartAngle,
                sweepAngleDegrees = sweepAngle
            )
        }

        drawPath(
            path = arcPath,
            color = color,
            style = Stroke(
                width = penThickness.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

@Preview
@Composable
fun CircularPulsatingIndicatorPreview() {
    Scaffold {
        Column(Modifier.padding(it)) {
            CircularPulsatingIndicator()
        }
    }
}