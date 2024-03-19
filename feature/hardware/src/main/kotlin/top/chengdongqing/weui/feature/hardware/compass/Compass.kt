package top.chengdongqing.weui.feature.hardware.compass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.chengdongqing.weui.core.utils.UpdatedEffect
import top.chengdongqing.weui.core.utils.formatDegree
import top.chengdongqing.weui.core.utils.polarToCartesian
import top.chengdongqing.weui.core.utils.vibrateShort

@Composable
fun WeCompass(compassViewModel: CompassViewModel = viewModel()) {
    val context = LocalContext.current
    val degrees = compassViewModel.degrees
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) // 加速度计
    val magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) // 磁力计

    LaunchedEffect(compassViewModel.observing) {
        if (compassViewModel.observing) {
            sensorManager.registerListener(
                compassViewModel.eventListener,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_UI
            )
            sensorManager.registerListener(
                compassViewModel.eventListener,
                magnetometerSensor,
                SensorManager.SENSOR_DELAY_UI
            )
        } else {
            sensorManager.unregisterListener(compassViewModel.eventListener)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            sensorManager.unregisterListener(compassViewModel.eventListener)
        }
    }
    // 指向准点方位后触发震动
    UpdatedEffect(degrees) {
        if (degrees % 90 == 0) {
            context.vibrateShort()
        }
    }

    CompassSurface(degrees)
}

@Composable
private fun CompassSurface(degrees: Int) {
    val textMeasurer = rememberTextMeasurer()
    val colors = MaterialTheme.compassColorScheme

    Canvas(
        modifier = Modifier
            .size(300.dp)
            .fillMaxSize()
    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        drawCompassFace(center, radius, colors)
        drawCompassScales(center, radius, degrees, textMeasurer, colors)
        drawNorthIndicator(radius, colors.indicatorColor)
        drawCurrentDegrees(radius, degrees, textMeasurer, colors)
    }
}

// 绘制圆盘
private fun DrawScope.drawCompassFace(center: Offset, radius: Float, colors: CompassColors) {
    drawCircle(
        color = colors.containerColor,
        center = center,
        radius = radius
    )
}

// 绘制罗盘的刻度和方位
private fun DrawScope.drawCompassScales(
    center: Offset,
    radius: Float,
    degrees: Int,
    textMeasurer: TextMeasurer,
    colors: CompassColors
) {
    for (angle in 0 until 360 step 10) {
        val angleRadians = Math.toRadians(angle.toDouble() - degrees - 90)
        drawScaleLine(center, radius, angle, angleRadians, colors)
        drawDegreeText(center, radius, angle, angleRadians, textMeasurer, colors)
        drawCardinalPoint(center, radius, angle, angleRadians, textMeasurer, colors)
    }
}

// 绘制刻度线
private fun DrawScope.drawScaleLine(
    center: Offset,
    radius: Float,
    angle: Int,
    angleRadians: Double,
    colors: CompassColors
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
        color = if (angle % 45 == 0) colors.scalePrimaryColor else colors.scaleSecondaryColor,
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
    textMeasurer: TextMeasurer,
    colors: CompassColors
) {
    if (angle % 30 == 0) {
        val degreeText = AnnotatedString(
            angle.toString(),
            SpanStyle(fontSize = 12.sp)
        )
        val textRadius = radius - 8.dp.toPx() - 26.dp.toPx()
        val (degreeX, degreeY) = polarToCartesian(center, textRadius, angleRadians)
        val textLayoutResult = textMeasurer.measure(degreeText)
        drawText(
            textLayoutResult,
            color = if (angle % 90 == 0) colors.scalePrimaryColor else colors.scaleSecondaryColor,
            topLeft = Offset(
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
    textMeasurer: TextMeasurer,
    colors: CompassColors
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
            textLayoutResult,
            color = colors.fontColor,
            topLeft = Offset(
                textX - textLayoutResult.size.width / 2,
                textY - textLayoutResult.size.height / 2
            )
        )
    }
}

// 绘制向北指针
private fun DrawScope.drawNorthIndicator(radius: Float, color: Color) {
    drawLine(
        color,
        Offset(x = radius, y = -8.dp.toPx()),
        Offset(x = radius, y = 18.dp.toPx()),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
}

// 绘制当前度数
private fun DrawScope.drawCurrentDegrees(
    radius: Float,
    degrees: Int,
    textMeasurer: TextMeasurer,
    colors: CompassColors
) {
    val degreeText = AnnotatedString(
        formatDegree(degrees.toFloat(), 0),
        SpanStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold)
    )
    val textLayoutResult = textMeasurer.measure(degreeText)
    drawText(
        textLayoutResult,
        color = colors.fontColor,
        topLeft = Offset(
            radius - textLayoutResult.size.width / 2,
            radius - textLayoutResult.size.height / 2
        )
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

private data class CompassColors(
    val containerColor: Color,
    val fontColor: Color,
    val scalePrimaryColor: Color,
    val scaleSecondaryColor: Color,
    val indicatorColor: Color
)

private val MaterialTheme.compassColorScheme: CompassColors
    @Composable
    get() = CompassColors(
        containerColor = colorScheme.onBackground,
        fontColor = colorScheme.onPrimary,
        scalePrimaryColor = colorScheme.onSecondary,
        scaleSecondaryColor = colorScheme.outline,
        indicatorColor = colorScheme.primary
    )