package top.chengdongqing.weui.feature.charts.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.generateColors
import top.chengdongqing.weui.feature.charts.model.ChartData

@Composable
fun WePieChart(
    dataSource: List<ChartData>,
    ringWidth: Dp = 0.dp,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 800),
    formatter: (Float) -> String = { it.format() }
) {
    // 数值之和
    val total = remember(dataSource) { dataSource.sumOf { it.value.toDouble() }.toFloat() }
    // 随机颜色
    val colors = remember(dataSource.size) { generateColors(dataSource.size) }
    // 每个数据项的动画实例
    val animatedSweepAngles = remember(dataSource.size) { dataSource.map { Animatable(0f) } }

    LaunchedEffect(dataSource) {
        // 累计角度，用于计算每个动画的起始角度
        var accumulatedAngle = 0f
        dataSource.forEachIndexed { index, item ->
            // 当前扇形的夹角度数
            val targetAngle = item.value / total * 360f
            // 当前扇形的结束角度
            val endAngle = accumulatedAngle + targetAngle
            launch {
                animatedSweepAngles[index].animateTo(
                    targetValue = endAngle,
                    animationSpec = animationSpec
                )
            }
            // 更新累计角度为下一个扇形计算起始角度
            accumulatedAngle += targetAngle
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        ChartLegends(dataSource, colors, formatter)
        PieFace(animatedSweepAngles, dataSource, colors, ringWidth)
    }
}

@Composable
private fun PieFace(
    animatedSweepAngles: List<Animatable<Float, AnimationVector1D>>,
    dataSource: List<ChartData>,
    colors: List<Color>,
    ringWidth: Dp
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth(0.6f)
            .aspectRatio(1f)
    ) {
        val innerRadius = if (ringWidth > 0.dp) {
            ringWidth.toPx() / 2f
        } else {
            0f
        }
        var startAngle = -90f
        animatedSweepAngles.forEachIndexed { index, item ->
            val sweepAngle =
                item.value - (if (index > 0) animatedSweepAngles[index - 1].value else 0f)
            if (innerRadius > 0f) {
                // 绘制环形图
                drawArc(
                    color = dataSource[index].color ?: colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(innerRadius, innerRadius),
                    size = Size(size.width - 2 * innerRadius, size.height - 2 * innerRadius),
                    style = Stroke(width = ringWidth.toPx())
                )
            } else {
                // 绘制饼图
                drawArc(
                    color = dataSource[index].color ?: colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset.Zero,
                    size = size
                )
            }
            startAngle += sweepAngle
        }
    }
}

@Composable
private fun ChartLegends(
    dataSource: List<ChartData>,
    colors: List<Color>,
    formatter: (Float) -> String
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        dataSource.forEachIndexed { index, item ->
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(item.color ?: colors[index], CircleShape)
                    )
                    Text(
                        text = item.label,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = formatter(item.value),
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 11.sp,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }
        }
    }
}