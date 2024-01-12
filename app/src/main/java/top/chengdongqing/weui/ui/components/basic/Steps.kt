package top.chengdongqing.weui.ui.components.basic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeSteps(
    value: Int = 0,
    items: List<(@Composable () -> Unit)?>,
    isVertical: Boolean = true
) {
    if (isVertical) {
        Column {
            StepItems(items, value, true)
        }
    } else {
        Row {
            StepItems(items, value, false)
        }
    }
}

@Composable
fun StepItems(items: List<(@Composable () -> Unit)?>, value: Int, isVertical: Boolean) {
    items.forEachIndexed { index, content ->
        StepItem(
            content,
            isActive = index <= value,
            isFirst = index == 0,
            isLast = index == items.size - 1,
            isLastActive = index == value,
            isVertical
        )
    }
}

@Composable
private fun StepItem(
    content: (@Composable () -> Unit)?,
    isActive: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    isLastActive: Boolean,
    isVertical: Boolean
) {
    Box(
        contentAlignment = if (isVertical) Alignment.TopStart else Alignment.TopCenter,
        modifier = Modifier
            .drawBehind {
                val color = if (isActive) PrimaryColor else BorderColor
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
                            color = if (isLastActive) BorderColor else color,
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
                            color = if (isLastActive) BorderColor else color,
                            start = Offset(size.width / 2 + offset, offset),
                            end = Offset(size.width, offset),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            }
            .padding(if (isVertical) PaddingValues(start = 36.dp) else PaddingValues(top = 36.dp))
            .sizeIn(
                if (isVertical) Dp.Unspecified else 80.dp,
                if (!isVertical) Dp.Unspecified else 80.dp
            )
    ) {
        content?.invoke()
    }
}
