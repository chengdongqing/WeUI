package top.chengdongqing.weui.ui.components.progress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun WeCircleProgress(
    percent: Float,
    size: Dp = 100.dp,
    strokeWidth: Dp = 6.dp,
    fontSize: TextUnit = 16.sp,
    formatter: ((percent: Float) -> String)? = {
        "${formatFloat(it)}%"
    }
) {
    val localPercent = percent.coerceIn(0f, 100f)
    val animatedAngle by animateFloatAsState(
        targetValue = 360 * (localPercent / 100),
        label = "CircleProgressAnimation"
    )
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .size(size)
    ) {
        val radius = this.size.width / 2
        drawCircle(
            color = Color(0f, 0f, 0f, 0.06f),
            radius = radius,
            style = Stroke(width = strokeWidth.toPx())
        )
        drawArc(
            color = PrimaryColor,
            startAngle = -90f,
            sweepAngle = animatedAngle,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
        formatter?.also {
            val text = AnnotatedString(
                it(localPercent),
                SpanStyle(fontSize = fontSize, color = FontColor)
            )
            val textLayoutResult = textMeasurer.measure(text)
            drawText(
                textMeasurer,
                text,
                Offset(
                    x = radius - textLayoutResult.size.width / 2,
                    y = radius - textLayoutResult.size.height / 2
                )
            )
        }
    }
}