package top.chengdongqing.weui.ui.components.clock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.utils.polarToCartesian
import java.time.Instant
import java.time.ZoneId
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WeClock(
    zoneId: ZoneId = ZoneId.systemDefault(),
    borderColor: Color = BorderColor,
    scale: Float = 1f
) {
    val (currentTime, setCurrentTime) = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(1000)
            setCurrentTime(System.currentTimeMillis())
        }
    }

    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = Modifier
            .size(300.dp)
            .scale(scale)
    ) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2
        val center = Offset(x = radius, y = radius)

        drawClockFace(radius, borderColor)
        drawClockScales(radius, center, textMeasurer)
        drawClockIndicators(radius, center, currentTime, zoneId)
        drawIndicatorsLock()
    }
}

// 绘制圆盘和边框
private fun DrawScope.drawClockFace(radius: Float, borderColor: Color) {
    // 绘制圆盘
    drawCircle(Color.White)
    // 绘制边框
    val borderWidth = 6.dp.toPx()
    drawCircle(
        color = borderColor,
        radius = radius - borderWidth / 2,
        style = Stroke(width = borderWidth)
    )
}

// 绘制刻度和数字
private fun DrawScope.drawClockScales(radius: Float, center: Offset, textMeasurer: TextMeasurer) {
    val localRadius = radius - 10.dp.toPx()
    for (i in 0 until 60) {
        val angle = (i * 6).toFloat()
        val startRadius = if (i % 5 == 0) {
            localRadius - 10.dp.toPx()
        } else {
            localRadius - 8.dp.toPx()
        }
        // 绘制刻度
        drawLine(
            color = if (i % 5 == 0) FontColor1 else LightColor,
            start = Offset(
                x = center.x + cos(Math.toRadians(angle.toDouble())).toFloat() * startRadius,
                y = center.y + sin(Math.toRadians(angle.toDouble())).toFloat() * startRadius
            ),
            end = Offset(
                x = center.x + cos(Math.toRadians(angle.toDouble())).toFloat() * localRadius,
                y = center.y + sin(Math.toRadians(angle.toDouble())).toFloat() * localRadius
            ),
            strokeWidth = if (i % 5 == 0) 6f else 2f
        )
        // 绘制数字
        if (i % 5 == 0) {
            val angleRadians = Math.toRadians(angle.toDouble() - 90)
            val text = AnnotatedString(
                (i / 5).let { if (it == 0) 12 else it }.toString(),
                SpanStyle(fontSize = 24.sp)
            )
            val textLayoutResult = textMeasurer.measure(text)
            val textRadius = radius - 40.dp.toPx()
            val (degreeX, degreeY) = polarToCartesian(center, textRadius, angleRadians)
            drawText(
                textMeasurer, text, Offset(
                    x = degreeX - textLayoutResult.size.width / 2,
                    y = degreeY - textLayoutResult.size.height / 2
                )
            )
        }
    }
}

// 绘制指针
private fun DrawScope.drawClockIndicators(
    radius: Float,
    center: Offset,
    currentTime: Long,
    zoneId: ZoneId
) {
    val time = Instant.ofEpochMilli(currentTime).atZone(zoneId).toLocalTime()
    val hours = time.hour % 12
    val minutes = time.minute
    val seconds = time.second

    val hourAngle = (hours + minutes / 60f) * 30f - 90
    val minuteAngle = minutes * 6f - 90
    val secondAngle = seconds * 6f - 90

    // 绘制时针
    drawLine(
        color = Color.Black,
        start = center,
        end = Offset(
            x = center.x + cos(Math.toRadians(hourAngle.toDouble())).toFloat() * radius / 2,
            y = center.y + sin(Math.toRadians(hourAngle.toDouble())).toFloat() * radius / 2
        ),
        strokeWidth = 10f,
        cap = StrokeCap.Round
    )
    // 绘制分针
    drawLine(
        color = Color.Black,
        start = center,
        end = Offset(
            x = center.x + cos(Math.toRadians(minuteAngle.toDouble())).toFloat() * radius / 1.5f,
            y = center.y + sin(Math.toRadians(minuteAngle.toDouble())).toFloat() * radius / 1.5f
        ),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )
    // 绘制秒针
    drawLine(
        color = Color.Red,
        start = center,
        end = Offset(
            x = center.x + cos(Math.toRadians(secondAngle.toDouble())).toFloat() * radius / 1.2f,
            y = center.y + sin(Math.toRadians(secondAngle.toDouble())).toFloat() * radius / 1.2f
        ),
        strokeWidth = 2f
    )
}

// 绘制指针锁
private fun DrawScope.drawIndicatorsLock() {
    drawCircle(Color.Black, 5.dp.toPx())
    drawCircle(Color.White, 3.dp.toPx())
}