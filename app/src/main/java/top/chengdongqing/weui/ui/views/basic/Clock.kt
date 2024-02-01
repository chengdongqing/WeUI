package top.chengdongqing.weui.ui.views.basic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.ui.views.hardware.compass.polarToCartesian
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ClockPage() {
    WePage(title = "Clock", description = "时钟") {
        val timeZones = remember {
            mapOf(
                Pair("上海（中国）", "Asia/Shanghai"),
                Pair("莫斯科（俄罗斯）", "Europe/Moscow"),
                Pair("伦敦（英格兰）", "Europe/London"),
                Pair("圣保罗（巴西）", "America/Sao_Paulo"),
                Pair("洛杉矶（美国）", "America/Los_Angeles"),
                Pair("悉尼（澳大利亚）", "Australia/Sydney")
            )
        }

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            timeZones.forEach {
                item {
                    Text(text = it.key, color = FontColo1, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    Clock(TimeZone.getTimeZone(it.value))
                }
            }
        }
    }
}

@Composable
fun Clock(timeZone: TimeZone = TimeZone.getDefault()) {
    val (currentTime, setCurrentTime) = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            setCurrentTime(System.currentTimeMillis())
        }
    }

    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.size(300.dp)) {
        val canvasSize = size.minDimension
        val radius = canvasSize / 2
        val center = Offset(x = radius, y = radius)

        drawClockFace(radius)
        drawClockScales(radius, center, textMeasurer)
        drawClockIndicators(radius, center, currentTime, timeZone)
        drawIndicatorLock()
    }
}

// 绘制圆盘和边框
private fun DrawScope.drawClockFace(radius: Float) {
    // 绘制圆盘
    drawCircle(Color.White)
    // 绘制边框
    val borderWidth = 6.dp.toPx()
    drawCircle(
        color = BorderColor,
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
            color = if (i % 5 == 0) FontColo1 else LightColor,
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
            println("i: $i")

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
    timeZone: TimeZone
) {
    val calendar = Calendar.getInstance(timeZone).apply {
        timeInMillis = currentTime
    }
    val hours = calendar.get(Calendar.HOUR_OF_DAY) % 12
    val minutes = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)

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
private fun DrawScope.drawIndicatorLock() {
    drawCircle(Color.Black, 5.dp.toPx())
    drawCircle(Color.White, 3.dp.toPx())
}