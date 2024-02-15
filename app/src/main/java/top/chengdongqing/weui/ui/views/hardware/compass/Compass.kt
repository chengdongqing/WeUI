package top.chengdongqing.weui.ui.views.hardware.compass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.formatDegree
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Compass(degrees: Int) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .size(300.dp)
            .fillMaxSize()
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        drawCompassFace(center, radius)
        drawCompassScales(center, radius, degrees, textMeasurer)
        drawNorthIndicator(radius)
        drawCurrentDegrees(radius, degrees, textMeasurer)
    }
}

// 绘制圆盘
private fun DrawScope.drawCompassFace(center: Offset, radius: Float) {
    drawCircle(
        color = Color.White,
        center = center,
        radius = radius
    )
}

// 绘制罗盘的刻度和方位
private fun DrawScope.drawCompassScales(
    center: Offset,
    radius: Float,
    degrees: Int,
    textMeasurer: TextMeasurer
) {
    for (angle in 0 until 360 step 10) {
        val angleRadians = Math.toRadians(angle.toDouble() - degrees - 90)
        drawScaleLine(center, radius, angle, angleRadians)
        drawDegreeText(center, radius, angle, angleRadians, textMeasurer)
        drawCardinalPoint(center, radius, angle, angleRadians, textMeasurer)
    }
}

// 绘制刻度线
private fun DrawScope.drawScaleLine(
    center: Offset,
    radius: Float,
    angle: Int,
    angleRadians: Double
) {
    val localRadius = radius - 8.dp.toPx()
    val startRadius = if (angle % 45 == 0) {
        localRadius - 10.dp.toPx()
    } else {
        localRadius - 8.dp.toPx()
    }
    val (startX, startY) = polarToCartesian(center, startRadius, angleRadians)
    val (endX, endY) = polarToCartesian(center, localRadius, angleRadians)

    drawLine(
        color = if (angle % 45 == 0) LightColor else BorderColor,
        start = Offset(startX, startY),
        end = Offset(endX, endY),
        strokeWidth = 1.dp.toPx()
    )
}

// 绘制度数刻度
private fun DrawScope.drawDegreeText(
    center: Offset,
    radius: Float,
    angle: Int,
    angleRadians: Double,
    textMeasurer: TextMeasurer
) {
    if (angle % 30 == 0) {
        val degreeText = AnnotatedString(
            angle.toString(),
            SpanStyle(
                fontSize = 12.sp,
                color = if (angle % 90 == 0) FontColor1 else BorderColor
            )
        )
        val textRadius = radius - 8.dp.toPx() - 26.dp.toPx()
        val (degreeX, degreeY) = polarToCartesian(center, textRadius, angleRadians)
        val textLayoutResult = textMeasurer.measure(degreeText)
        drawText(
            textMeasurer,
            degreeText,
            Offset(
                degreeX - textLayoutResult.size.width / 2,
                degreeY - textLayoutResult.size.height / 2
            )
        )
    }
}

// 绘制方位文本
private fun DrawScope.drawCardinalPoint(
    center: Offset,
    radius: Float,
    angle: Int,
    angleRadians: Double,
    textMeasurer: TextMeasurer
) {
    if (angle % 90 == 0) {
        val cardinalPoint = getCardinalPoint(angle)
        val textRadius = radius - 8.dp.toPx() - 60.dp.toPx()
        val (textX, textY) = polarToCartesian(center, textRadius, angleRadians)
        val text = AnnotatedString(
            cardinalPoint,
            SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
        val textLayoutResult = textMeasurer.measure(text)
        drawText(
            textMeasurer,
            text,
            Offset(
                textX - textLayoutResult.size.width / 2,
                textY - textLayoutResult.size.height / 2
            )
        )
    }
}

// 绘制向北指针
private fun DrawScope.drawNorthIndicator(radius: Float) {
    drawLine(
        PrimaryColor,
        Offset(x = radius, y = -8.dp.toPx()),
        Offset(x = radius, y = 18.dp.toPx()),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
}

// 绘制当前度数
private fun DrawScope.drawCurrentDegrees(radius: Float, degrees: Int, textMeasurer: TextMeasurer) {
    val degreeText = AnnotatedString(
        formatDegree(degrees.toFloat()),
        SpanStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold)
    )
    val degreesLayoutResult = textMeasurer.measure(degreeText)
    drawText(
        textMeasurer, degreeText, Offset(
            radius - degreesLayoutResult.size.width / 2,
            radius - degreesLayoutResult.size.height / 2,
        )
    )
}

// 将极坐标转换为笛卡尔坐标
fun polarToCartesian(
    center: Offset,
    radius: Float,
    angleRadians: Double
): Pair<Float, Float> {
    return Pair(
        center.x + (radius * cos(angleRadians)).toFloat(),
        center.y + (radius * sin(angleRadians)).toFloat()
    )
}

// 获取方位名称
private fun getCardinalPoint(angle: Int): String {
    return when (angle) {
        0 -> "北"
        90 -> "东"
        180 -> "南"
        270 -> "西"
        else -> ""
    }
}