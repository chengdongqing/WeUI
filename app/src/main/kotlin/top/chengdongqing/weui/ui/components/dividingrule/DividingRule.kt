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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.PrimaryColor

enum class RuleDirection {
    HORIZONTAL,
    VERTICAL
}

@Composable
fun WeDividingRule(
    start: Int = 0,
    end: Int = 100,
    step: Int = 10,
    direction: RuleDirection = RuleDirection.HORIZONTAL,
    colors: DividingRuleColors = defaultColors
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colors.containerColor)
    ) {
        Rule(start, end, step, colors.contentColor)
        Indicator(colors.indicatorColor)
    }
}

@Composable
private fun Rule(start: Int, end: Int, step: Int, color: Color) {
    val textMeasurer = rememberTextMeasurer()
    val count = (end - start) / step

    Box(modifier = Modifier.horizontalScroll(rememberScrollState())) {
        Canvas(
            modifier = Modifier
                .width((80 * count + 50 * 2).dp)
                .height(80.dp)
                .padding(horizontal = 50.dp)
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
            y = 40.dp.toPx()
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
            y = 40.dp.toPx()
        )
    )
}

@Composable
private fun Indicator(color: Color) {
    Canvas(modifier = Modifier.offset(x = (50 - 12 / 2).dp, y = (-30).dp)) {
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
    val containerColor: Color = Color.White,
    val contentColor: Color = Color.Black,
    val indicatorColor: Color = PrimaryColor
)

private val defaultColors: DividingRuleColors
    @Composable
    get() {
        return if (isSystemInDarkTheme()) {
            DividingRuleColors(containerColor = Color.Black, contentColor = Color.White)
        } else {
            DividingRuleColors()
        }
    }