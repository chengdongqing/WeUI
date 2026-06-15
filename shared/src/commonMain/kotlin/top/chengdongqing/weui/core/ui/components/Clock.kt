package top.chengdongqing.weui.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import top.chengdongqing.weui.core.ui.theme.WeColorScheme
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.polarToCartesian
import top.chengdongqing.weui.util.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@Composable
fun WeClock(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    borderColor: Color = WeTheme.colorScheme.divider,
    scale: Float = 1f,
    isSmoothSweep: Boolean = false // 是否启用流线扫秒
) {
    val currentTimeState = remember { mutableLongStateOf(Clock.System.now().toEpochMilliseconds()) }

    LaunchedEffect(isSmoothSweep) {
        while (isActive) {
            if (isSmoothSweep) {
                // 随屏幕刷新率更新
                withFrameMillis {
                    currentTimeState.longValue = Clock.System.now().toEpochMilliseconds()
                }
            } else {
                // 每秒更新一次
                delay(1000.milliseconds)
                currentTimeState.longValue = Clock.System.now().toEpochMilliseconds()
            }
        }
    }

    val textMeasurer = rememberTextMeasurer()
    val colorScheme = WeTheme.colorScheme

    Canvas(
        modifier = Modifier
            .size(300.dp)
            .scale(scale)
            // 缓存静态内容
            .drawWithCache {
                onDrawWithContent {
                    val canvasSize = size.minDimension
                    val radius = canvasSize / 2
                    val center = Offset(x = radius, y = radius)

                    // A. 绘制静态部分
                    drawClockFace(radius, borderColor, colorScheme)
                    drawClockScales(radius, center, textMeasurer, colorScheme)

                    // B. 绘制动态部分
                    // 这里会高频执行
                    drawClockIndicators(
                        radius,
                        center,
                        currentTimeState.longValue,
                        timeZone,
                        colorScheme,
                        isSmoothSweep
                    )
                    drawIndicatorsLock(colorScheme)
                }
            }
    ) { }
}

// 绘制圆盘和边框
private fun DrawScope.drawClockFace(radius: Float, borderColor: Color, colorScheme: WeColorScheme) {
    // 绘制圆盘
    drawCircle(colorScheme.surface)
    // 绘制边框
    val borderWidth = 6.dp.toPx()
    drawCircle(
        color = borderColor,
        radius = radius - borderWidth / 2,
        style = Stroke(width = borderWidth)
    )
}

// 绘制刻度和数字
private fun DrawScope.drawClockScales(
    radius: Float,
    center: Offset,
    textMeasurer: TextMeasurer,
    colorScheme: WeColorScheme
) {
    val localRadius = radius - 10.dp.toPx()
    for (i in 0 until 60) {
        val angle = (i * 6).toFloat()
        val angleRad = angle.toDouble().toRadians()
        val startRadius = if (i % 5 == 0) {
            localRadius - 10.dp.toPx()
        } else {
            localRadius - 8.dp.toPx()
        }
        val isMajor = i % 5 == 0

        // 绘制刻度
        drawLine(
            color = if (isMajor) colorScheme.textSecondary else colorScheme.divider,
            start = Offset(
                x = center.x + cos(angleRad).toFloat() * startRadius,
                y = center.y + sin(angleRad).toFloat() * startRadius
            ),
            end = Offset(
                x = center.x + cos(angleRad).toFloat() * localRadius,
                y = center.y + sin(angleRad).toFloat() * localRadius
            ),
            strokeWidth = if (isMajor) 6f else 2f
        )
        // 绘制数字
        if (isMajor) {
            val angleRadians = (angle.toDouble() - 90).toRadians()
            val text = AnnotatedString(
                (i / 5).let { if (it == 0) 12 else it }.toString(),
                SpanStyle(fontSize = 24.sp)
            )
            val textLayoutResult = textMeasurer.measure(text)
            val textRadius = radius - 40.dp.toPx()
            val (degreeX, degreeY) = polarToCartesian(center, textRadius, angleRadians)
            drawText(
                textLayoutResult,
                color = colorScheme.textPrimary,
                topLeft = Offset(
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
    timeZone: TimeZone,
    colorScheme: WeColorScheme,
    isSmoothSweep: Boolean
) {
    val instant = Instant.fromEpochMilliseconds(currentTime)
    val localTime = instant.toLocalDateTime(timeZone).time

    val hours = localTime.hour % 12
    val minutes = localTime.minute
    val seconds = localTime.second

    // 计算平滑偏移量
    val millisOffset = if (isSmoothSweep) {
        (currentTime % 1000) / 1000f
    } else {
        0f
    }

    val hourAngle = (hours + minutes / 60f) * 30f - 90
    val minuteAngle = minutes * 6f - 90
    val secondAngle = (seconds + millisOffset) * 6f - 90

    // 绘制时针
    val hourAngleRad = hourAngle.toDouble().toRadians()
    drawLine(
        color = colorScheme.textPrimary,
        start = center,
        end = Offset(
            x = center.x + cos(hourAngleRad).toFloat() * radius / 2,
            y = center.y + sin(hourAngleRad).toFloat() * radius / 2
        ),
        strokeWidth = 10f,
        cap = StrokeCap.Round
    )
    // 绘制分针
    val minuteAngleRad = minuteAngle.toDouble().toRadians()
    drawLine(
        color = colorScheme.textPrimary,
        start = center,
        end = Offset(
            x = center.x + cos(minuteAngleRad).toFloat() * radius / 1.6f,
            y = center.y + sin(minuteAngleRad).toFloat() * radius / 1.6f
        ),
        strokeWidth = 6f,
        cap = StrokeCap.Round
    )
    // 绘制秒针
    val secondAngleRad = secondAngle.toDouble().toRadians()
    drawLine(
        color = Color.Red,
        start = center,
        end = Offset(
            x = center.x + cos(secondAngleRad).toFloat() * radius / 1.2f,
            y = center.y + sin(secondAngleRad).toFloat() * radius / 1.2f
        ),
        strokeWidth = 2f
    )
}

// 绘制指针锁
private fun DrawScope.drawIndicatorsLock(colorScheme: WeColorScheme) {
    drawCircle(colorScheme.textPrimary, 5.dp.toPx())
    drawCircle(colorScheme.surface, 3.dp.toPx())
}