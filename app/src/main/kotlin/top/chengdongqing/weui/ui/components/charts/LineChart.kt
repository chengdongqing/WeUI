package top.chengdongqing.weui.ui.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.format

data class LineChartData(val points: List<ChartData>, val color: Color)

@Composable
fun WeLineChart(
    dataSources: List<LineChartData>,
    height: Dp = 300.dp,
    color: Color = PrimaryColor.copy(0.8f),
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 800),
    formatter: (Float) -> String = { it.format() }
) {
    val textMeasurer = rememberTextMeasurer()
    val labelColor = MaterialTheme.colorScheme.onSecondary
    val containerColor = MaterialTheme.colorScheme.onBackground

    // 计算所有数据源中的最大值
    val maxValue = remember(dataSources) {
        dataSources.flatMap { it.points }.maxOfOrNull { it.value } ?: 1f
    }

    // 为每个数据点创建动画实例
    val animatedValuesList = remember(dataSources.size) {
        dataSources.map { dataSource ->
            dataSource.points.map { Animatable(0f) }
        }
    }

    // 数据变化后执行动画
    LaunchedEffect(dataSources) {
        animatedValuesList.forEachIndexed { dataSourceIndex, animatedValues ->
            animatedValues.forEachIndexed { index, item ->
                launch {
                    item.animateTo(
                        targetValue = dataSources[dataSourceIndex].points[index].value / maxValue,
                        animationSpec = animationSpec
                    )
                }
            }
        }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .height(height)
    ) {
        // 使用第一个数据源的标签
        val labels = dataSources.firstOrNull()?.points?.map { it.label } ?: emptyList()

        // 绘制X轴
        drawXAxis(
            labels = labels,
            size.width / labels.size,
            0f,
            axisColor = color,
            labelColor,
            textMeasurer
        )

        // 为每个数据源绘制折线和数据点
        dataSources.forEachIndexed { index, dataSource ->
            drawLines(
                animatedValuesList[index],
                dataSource.points,
                lineColor = dataSource.color,
                containerColor = containerColor,
                textMeasurer,
                formatter
            )
        }
    }
}

private fun DrawScope.drawLines(
    animatedValues: List<Animatable<Float, AnimationVector1D>>,
    dataSource: List<ChartData>,
    lineColor: Color,
    containerColor: Color,
    textMeasurer: TextMeasurer,
    formatter: (Float) -> String
) {
    val pointWidth = 1.5.dp.toPx()
    val pointSpace = (size.width - pointWidth * dataSource.size) / dataSource.size

    animatedValues.draw(pointWidth, pointSpace, size.height) { currentPoint, previousPoint, index ->
        // 绘制数值标签
        if (pointSpace >= 10.dp.toPx()) {
            drawValueLabel(
                value = dataSource[index].value,
                currentPoint.x,
                currentPoint.y,
                textMeasurer,
                formatter,
                lineColor
            )
        }
        // 连接数据点
        previousPoint?.let {
            drawLine(
                color = lineColor,
                start = it,
                end = currentPoint,
                strokeWidth = pointWidth
            )
        }
    }

    // 绘制数据点
    animatedValues.draw(pointWidth, pointSpace, size.height) { currentPoint, _, _ ->
        drawCircle(color = lineColor, radius = 3.dp.toPx(), center = currentPoint)
        drawCircle(color = containerColor, radius = 1.5.dp.toPx(), center = currentPoint)
    }
}

private fun List<Animatable<Float, AnimationVector1D>>.draw(
    pointWidth: Float,
    pointSpace: Float,
    height: Float,
    block: (currentPoint: Offset, previousPoint: Offset?, index: Int) -> Unit
) {
    var previousPoint: Offset? = null
    this.forEachIndexed { index, item ->
        val x = index * (pointWidth + pointSpace) + pointSpace / 2
        val y = height - (item.value * height)
        val currentPoint = Offset(x, y)
        block(currentPoint, previousPoint, index)
        previousPoint = currentPoint
    }
}

private fun DrawScope.drawValueLabel(
    value: Float,
    offsetX: Float,
    offsetY: Float,
    textMeasurer: TextMeasurer,
    valueFormatter: (Float) -> String,
    textColor: Color
) {
    val valueText = valueFormatter(value)
    val textLayoutResult = textMeasurer.measure(
        valueText,
        TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold)
    )
    drawText(
        textLayoutResult,
        textColor,
        Offset(
            offsetX - textLayoutResult.size.width / 2,
            offsetY - textLayoutResult.size.height - 5.dp.toPx()
        )
    )
}