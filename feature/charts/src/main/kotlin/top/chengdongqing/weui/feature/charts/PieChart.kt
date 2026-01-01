package top.chengdongqing.weui.feature.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import top.chengdongqing.weui.core.utils.generateColors
import top.chengdongqing.weui.feature.charts.model.ChartData
import top.chengdongqing.weui.feature.charts.model.PieChartLegendItem

@Composable
fun WePieChart(
    dataSource: List<ChartData>,
    modifier: Modifier = Modifier,
    ringWidth: Dp = 0.dp,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 800),
    formatter: (Float) -> String = { it.toString() },
    legendContent: @Composable ((List<PieChartLegendItem>) -> Unit)?
) {
    val total = remember(dataSource) { dataSource.sumOf { it.value.toDouble() }.toFloat() }
    val colors = remember(dataSource.size) { generateColors(dataSource.size) }
    val animatedSweepAngles = remember(dataSource.size) { dataSource.map { Animatable(0f) } }

    // 计算图例所需的数据
    val legendItems = remember(dataSource, colors, total) {
        dataSource.mapIndexed { index, item ->
            PieChartLegendItem(
                label = item.label,
                value = item.value,
                color = item.color ?: colors[index],
                percentage = if (total > 0f) item.value / total else 0f,
                formattedValue = formatter(item.value)
            )
        }
    }

    LaunchedEffect(dataSource) {
        var accumulatedAngle = 0f
        dataSource.forEachIndexed { index, item ->
            val targetAngle = if (total > 0) (item.value / total * 360f) else 0f
            val endAngle = accumulatedAngle + targetAngle
            launch {
                animatedSweepAngles[index].animateTo(endAngle, animationSpec)
            }
            accumulatedAngle += targetAngle
        }
    }

    // 渲染饼图
    PieFace(
        modifier = modifier
            .aspectRatio(1f),
        animatedSweepAngles = animatedSweepAngles,
        legendItems = legendItems,
        ringWidth = ringWidth
    )

    // 渲染图例
    legendContent?.invoke(legendItems)
}

@Composable
private fun PieFace(
    modifier: Modifier,
    animatedSweepAngles: List<Animatable<Float, AnimationVector1D>>,
    legendItems: List<PieChartLegendItem>,
    ringWidth: Dp
) {
    Canvas(modifier = modifier) {
        val strokeWidthPx = ringWidth.toPx()
        val isDonut = strokeWidthPx > 0f // 环形图
        var startAngle = -90f

        animatedSweepAngles.forEachIndexed { index, animatable ->
            val currentTotalAngle = animatable.value
            val previousTotalAngle = if (index > 0) animatedSweepAngles[index - 1].value else 0f
            val sweepAngle = currentTotalAngle - previousTotalAngle

            if (isDonut) {
                drawArc(
                    color = legendItems[index].color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                    size = Size(size.width - strokeWidthPx, size.height - strokeWidthPx),
                    style = Stroke(width = strokeWidthPx)
                )
            } else {
                drawArc(
                    color = legendItems[index].color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = size
                )
            }
            startAngle += sweepAngle
        }
    }
}

@Composable
fun DefaultChartLegend(
    items: List<PieChartLegendItem>,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(item.color, CircleShape)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "${item.label} (${item.formattedValue})",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
