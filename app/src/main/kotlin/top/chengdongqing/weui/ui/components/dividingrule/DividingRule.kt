package top.chengdongqing.weui.ui.components.dividingrule

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeDividingRule(
    range: IntProgression = 0..100 step 2,
    colors: DividingRuleColors = DividingRuleDefaults.colors,
    onChange: (Float) -> Unit
) {
    val density = LocalDensity.current
    var halfWidth by remember { mutableStateOf(0.dp) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colors.containerColor)
            .onSizeChanged {
                halfWidth = density.run { (it.width / 2).toDp() }
            }
    ) {
        Rule(range, colors.contentColor, halfWidth, onChange)
        Indicator(colors.indicatorColor, halfWidth)
    }
}

@Composable
private fun Rule(
    range: IntProgression,
    color: Color,
    horizontalPadding: Dp,
    onChange: (Float) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()
    val scrollState = rememberScrollState()

    val start = range.first
    val end = range.last
    val step = range.step
    val count = (end - start) / step
    val widthDp = (80 * count).dp
    val density = LocalDensity.current

    LaunchedEffect(scrollState, widthDp) {
        val widthPx = density.run { widthDp.toPx() }
        val scaleCount = count * 10
        val valuePerScale = (end - start) / scaleCount
        val widthPerScale = widthPx / scaleCount

        snapshotFlow {
            scrollState.value
        }.collect { offset ->
            val value = start + offset / widthPerScale * valuePerScale
            onChange(value)
        }
    }

    Box(modifier = Modifier.horizontalScroll(scrollState)) {
        Canvas(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .width(widthDp)
                .height(80.dp)
        ) {
            repeat(count) { index ->
                drawRuleUnit(
                    value = (start + (step * index)).toString(),
                    color,
                    offset = index,
                    textMeasurer
                )
            }
            drawRuleClosureScale(
                value = end.toString(),
                color,
                offset = count,
                textMeasurer
            )
        }
    }
}

private fun DrawScope.drawRuleUnit(
    value: String,
    color: Color,
    offset: Int,
    textMeasurer: TextMeasurer
) {
    val offsetX = offset * 80.dp.toPx()

    val space = 8.dp.toPx()
    repeat(10) { index ->
        val x = index * space + offsetX
        val endY = when (index) {
            0 -> 40.dp.toPx()
            5 -> 30.dp.toPx()
            else -> 20.dp.toPx()
        }
        drawLine(
            color,
            start = Offset(x, 0f),
            end = Offset(x, endY),
            strokeWidth = 1.dp.toPx()
        )
    }

    val textLayoutResult = textMeasurer.measure(
        value,
        TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
    )
    drawText(
        textLayoutResult,
        color,
        Offset(
            x = -(textLayoutResult.size.width / 2f) + offsetX,
            y = 42.dp.toPx()
        )
    )
}

private fun DrawScope.drawRuleClosureScale(
    value: String,
    color: Color,
    offset: Int,
    textMeasurer: TextMeasurer
) {
    val offsetX = offset * 80.dp.toPx()

    drawLine(
        color,
        start = Offset(offsetX, 0f),
        end = Offset(offsetX, 40.dp.toPx()),
        strokeWidth = 1.dp.toPx()
    )

    val textLayoutResult = textMeasurer.measure(
        value,
        TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
    )
    drawText(
        textLayoutResult,
        color,
        Offset(
            x = -(textLayoutResult.size.width / 2f) + offsetX,
            y = 42.dp.toPx()
        )
    )
}

@Composable
private fun Indicator(color: Color, offsetX: Dp) {
    Canvas(modifier = Modifier.offset(x = offsetX - (12 / 2).dp, y = (-12).dp)) {
        val path = Path().apply {
            val size = 12.dp.toPx()
            moveTo(0f, 0f)
            lineTo(size, 0f)
            lineTo(size, size)
            lineTo(size / 2, size * 2)
            lineTo(0f, size)
            lineTo(0f, 0f)
            close()
        }
        drawPath(path, color)
    }
}

data class DividingRuleColors(
    val containerColor: Color,
    val contentColor: Color,
    val indicatorColor: Color = PrimaryColor
)

object DividingRuleDefaults {
    val colors: DividingRuleColors
        @Composable
        get() {
            return if (isSystemInDarkTheme()) {
                DividingRuleColors(containerColor = Color.Black, contentColor = Color.White)
            } else {
                DividingRuleColors(containerColor = Color.White, contentColor = Color.Black)
            }
        }
}