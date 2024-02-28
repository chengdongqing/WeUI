package top.chengdongqing.weui.ui.components.steps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeSteps(
    options: List<(@Composable () -> Unit)?>,
    value: Int = 0,
    isVertical: Boolean = true
) {
    if (isVertical) {
        Column {
            StepList(options, value, true)
        }
    } else {
        Row {
            StepList(options, value, false)
        }
    }
}

@Composable
fun StepList(items: List<(@Composable () -> Unit)?>, value: Int, isVertical: Boolean) {
    items.forEachIndexed { index, content ->
        StepItem(
            isActive = index <= value,
            isFirst = index == 0,
            isLast = index == items.lastIndex,
            isLastActive = index == value,
            isVertical,
            content
        )
    }
}

@Composable
private fun StepItem(
    isActive: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    isLastActive: Boolean,
    isVertical: Boolean,
    content: (@Composable () -> Unit)?
) {
    val defaultColor = MaterialTheme.colorScheme.outline

    Box(
        contentAlignment = if (isVertical) Alignment.TopStart else Alignment.TopCenter,
        modifier = Modifier
            .drawBehind {
                val color = if (isActive) PrimaryColor else defaultColor
                val offset = 12.dp.toPx()
                // 绘制小圆点
                drawCircle(
                    color = color,
                    radius = 4.dp.toPx(),
                    center = Offset(if (isVertical) offset else size.width / 2, offset)
                )

                // 绘制连接线
                if (isVertical) {
                    if (!isLast) {
                        drawLine(
                            color = if (isLastActive) defaultColor else color,
                            start = Offset(offset, offset * 2),
                            end = Offset(offset, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                } else {
                    if (!isFirst) {
                        drawLine(
                            color = color,
                            start = Offset(0f, offset),
                            end = Offset(size.width / 2 - offset, offset),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                    if (!isLast) {
                        drawLine(
                            color = if (isLastActive) defaultColor else color,
                            start = Offset(size.width / 2 + offset, offset),
                            end = Offset(size.width, offset),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
            .padding(if (isVertical) PaddingValues(start = 36.dp) else PaddingValues(top = 36.dp))
            .sizeIn(
                if (isVertical) 0.dp else 80.dp,
                if (!isVertical) 0.dp else 80.dp
            )
    ) {
        content?.invoke()
    }
}
