package top.chengdongqing.weui.feature.hardware.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin

private val GridColor = Color.White.copy(alpha = 0.1f)
private val RadarGreen = Color(0xFF00FF41)

@Composable
fun GnssSkyView(
    satellites: List<SatelliteInfo>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelSmall.copy(
        fontSize = 10.sp,
        color = Color.White.copy(alpha = 0.5f)
    )

    // 动画状态
    val infiniteTransition = rememberInfiniteTransition(label = "Radar")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing)),
        label = "Rotation"
    )

    // 手机方位角
    val heading by rememberHeading(context)

    // 数据预处理
    val processedSatellites by remember(satellites) {
        derivedStateOf {
            prepareSatelliteDrawStates(satellites)
        }
    }

    // 当前点击选中的卫星编号
    var selectedSvid by remember { mutableStateOf<Int?>(null) }
    // 当前信号最强的前5颗卫星编号
    val top5Svid = remember(satellites) {
        satellites.sortedByDescending { it.cn0 }.take(5).map { it.svid }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black, RoundedCornerShape(8.dp))
            .padding(24.dp)
    ) {
        TypeLegend()
        Spacer(modifier = Modifier.height(40.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                // 点击卫星显示编号
                .onSatelliteTap(satellites) {
                    selectedSvid = it
                }
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width / 2

            // 绘制圆盘
            drawSkyGrid(center, radius, textMeasurer, labelStyle)
            // 雷达扫描动画
            drawRadarScanner(rotation, center, radius)
            // 绘制方向箭头
            drawHeadingArrow(heading, center)
            // 绘制卫星分布图
            drawSatellites(
                processedSatellites,
                center,
                radius,
                textMeasurer,
                labelStyle,
                selectedSvid,
                top5Svid
            )
        }
    }
}

private fun Modifier.onSatelliteTap(satellites: List<SatelliteInfo>, onTap: (svid: Int?) -> Unit) =
    pointerInput(satellites) {
        detectTapGestures { tapOffset ->
            val radius = size.width / 2f
            val center = Offset(size.width / 2f, size.height / 2f)

            // 寻找距离点击位置最近且在阈值内的卫星
            val clickedSat = satellites.find { sat ->
                // 坐标计算逻辑要与绘制逻辑一致
                val az = sat.azimuthDegrees.filter { it.isDigit() || it == '.' }
                    .toFloatOrNull() ?: 0f
                val el = sat.elevationDegrees.filter { it.isDigit() || it == '.' }
                    .toFloatOrNull() ?: 0f
                val dist = ((90f - el.coerceIn(0f, 90f)) / 90f) * radius
                val angleRad = toRadians((az - 90.0)).toFloat()
                val satPos = Offset(
                    center.x + dist * cos(angleRad),
                    center.y + dist * sin(angleRad)
                )

                // 如果点击点距离卫星中心小于20dp，则判定为选中
                (tapOffset - satPos).getDistance() <= 20.dp.toPx()
            }

            // 如果点到了卫星就选中它，点到空白处就取消选中
            onTap(clickedSat?.svid)
        }
    }

private fun DrawScope.drawSkyGrid(
    center: Offset,
    radius: Float,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle
) {
    // 绘制圆环
    listOf(0f, 30f, 60f).forEach { el ->
        val r = ((90f - el) / 90f) * radius
        drawCircle(
            color = GridColor,
            radius = r,
            style = Stroke(
                width = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )
        )
    }
    // 十字线
    drawLine(GridColor, Offset(center.x, 0f), Offset(center.x, size.height))
    drawLine(GridColor, Offset(0f, center.y), Offset(size.width, center.y))

    // 绘制 N E S W
    val directions = listOf("N", "E", "S", "W")
    directions.forEachIndexed { index, text ->
        val result = textMeasurer.measure(text, labelStyle)
        val w = result.size.width.toFloat()
        val h = result.size.height.toFloat()
        val verticalGap = 4f // N 和 S 离线的距离
        val horizontalGap = 12f // E 和 W 离线的距离
        val pos = when (index) {
            0 -> Offset(center.x - w / 2, center.y - radius - h - verticalGap)
            1 -> Offset(center.x + radius + horizontalGap, center.y - h / 2)
            2 -> Offset(center.x - w / 2, center.y + radius + verticalGap)
            else -> Offset(center.x - radius - w - horizontalGap, center.y - h / 2)
        }

        drawText(result, topLeft = pos)

    }
}

private fun DrawScope.drawRadarScanner(rotation: Float, center: Offset, radius: Float) {
    rotate(degrees = rotation, pivot = center) {
        drawArc(
            brush = Brush.sweepGradient(
                0.85f to Color.Transparent,
                1.0f to Color.Cyan.copy(alpha = 0.4f),
                center = center
            ),
            startAngle = -60f,
            sweepAngle = 60f,
            useCenter = true,
            size = Size(radius * 2, radius * 2),
            topLeft = Offset(center.x - radius, center.y - radius)
        )
    }
}

private fun DrawScope.drawSatellites(
    satellites: List<SatelliteDrawState>,
    center: Offset,
    radius: Float,
    textMeasurer: TextMeasurer,
    labelStyle: TextStyle,
    selectedSvid: Int?,
    top5Svid: List<Int>
) {
    satellites.forEach { sat ->
        // 通过预计算的相对偏移还原物理坐标
        val actualPos = center + (sat.relativeOffset * radius)
        // 基于信号强度计算尺寸
        val dotRadius = 5.dp.toPx() * sat.strengthFactor

        // 绘制卫星小圆点
        drawCircle(sat.color.copy(alpha = 0.2f), radius = dotRadius * 2f, center = actualPos)
        drawCircle(sat.color, radius = dotRadius, center = actualPos)

        // 绘制卫星编号，显示的条件为信号强度前五或当前点击的
        val shouldShowLabel = sat.svid == selectedSvid || sat.svid in top5Svid
        if (shouldShowLabel) {
            val textResult = textMeasurer.measure(sat.svid.toString(), labelStyle)
            val textWidth = textResult.size.width.toFloat()
            val textHeight = textResult.size.height.toFloat()
            val labelOffset = Offset(
                x = if (sat.relativeOffset.x >= 0) actualPos.x + dotRadius + 4f else actualPos.x - textWidth - dotRadius - 4f,
                y = if (sat.relativeOffset.y >= 0) actualPos.y + 4f else actualPos.y - textHeight - 4f
            )

            drawText(
                textMeasurer,
                sat.svid.toString(),
                labelOffset,
                style = labelStyle.copy(
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

private fun DrawScope.drawHeadingArrow(heading: Float, center: Offset) {
    rotate(degrees = heading, pivot = center) {
        val path = Path().apply {
            moveTo(center.x, center.y - 12.dp.toPx())
            lineTo(center.x - 7.dp.toPx(), center.y + 8.dp.toPx())
            lineTo(center.x, center.y + 4.dp.toPx())
            lineTo(center.x + 7.dp.toPx(), center.y + 8.dp.toPx())
            close()
        }
        drawPath(path, color = RadarGreen)
    }
}

private data class SatelliteDrawState(
    val svid: Int,
    val color: Color,
    val strengthFactor: Float,
    // 相对于中心点的偏移比例
    val relativeOffset: Offset
)

private fun prepareSatelliteDrawStates(satellites: List<SatelliteInfo>): List<SatelliteDrawState> {
    return satellites.map { sat ->
        // 解析数据
        val az = sat.azimuthDegrees.filter { it.isDigit() || it == '.' }.toFloatOrNull() ?: 0f
        val el = sat.elevationDegrees.filter { it.isDigit() || it == '.' }.toFloatOrNull() ?: 0f

        // 计算归一化距离 (0f 到 1f)
        val distRatio = (90f - el.coerceIn(0f, 90f)) / 90f
        val angleRad = toRadians((az - 90.0)).toFloat()

        // 计算相对中心点的偏移比例
        val relX = distRatio * cos(angleRad)
        val relY = distRatio * sin(angleRad)

        SatelliteDrawState(
            svid = sat.svid,
            color = SatelliteType.getById(sat.constellationType).color,
            strengthFactor = (sat.cn0 / 35f).coerceIn(0.4f, 1.2f),
            relativeOffset = Offset(relX, relY)
        )
    }
}

@Composable
fun TypeLegend(modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 4
    ) {
        SatelliteType.entries.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                // 颜色小圆点
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(item.color, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                // 星座名称
                Text(
                    text = item.shortName,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun rememberHeading(context: Context): State<Float> {
    val heading = remember { mutableStateOf(0f) }
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    DisposableEffect(Unit) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                    val rotationMatrix = FloatArray(9)
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(rotationMatrix, orientation)
                    // orientation[0] 是绕 Z 轴的弧度，转换为角度
                    heading.value = Math.toDegrees(orientation[0].toDouble()).toFloat()
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_UI)

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }
    return heading
}
