package top.chengdongqing.weui.animation.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import top.chengdongqing.weui.components.WeScreen
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RoseCurveScreen(onBack: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "rose")
    val k by transition.animateFloat(
        initialValue = 1f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(5000), RepeatMode.Reverse)
    )

    val primaryColor = MaterialTheme.colorScheme.primary

    WeScreen(
        title = "RoseCurve",
        description = "玫瑰曲线",
        onBack = onBack
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(1f)
        ) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val path = Path()

            for (i in 0..720) {
                val angle = i * PI / 180f
                val r = 300f * sin(k * angle)
                val x = (r * cos(angle) + centerX).toFloat()
                val y = (r * sin(angle) + centerY).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(width = 3f)
            )
        }
    }
}