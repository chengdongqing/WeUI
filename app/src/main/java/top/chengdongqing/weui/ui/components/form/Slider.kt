package top.chengdongqing.weui.ui.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.PrimaryColor

@Composable
fun WeSlider(
    value: Int,
    step: Int = 1,
    min: Int = 0,
    max: Int = 100,
    onValueChange: (value: Int) -> Unit
) {
    val density = LocalDensity.current
    var sliderWidth by remember { mutableIntStateOf(0) }
    var fraction by remember { mutableFloatStateOf(0f) }
    var offsetX by remember { mutableStateOf(0.dp) }

    // 值改变后计算相应元素的位置或宽度
    LaunchedEffect(value, sliderWidth) {
        fraction = (value.coerceIn(min, max) - min) / (max - min).toFloat()
        offsetX = density.run { (sliderWidth * fraction).toDp() }
    }

    // 处理滑动或点击事件
    fun handleChange(offsetX: Float) {
        val newFraction = (offsetX / sliderWidth).coerceIn(0f, 1f)
        val newValue = (newFraction * (max - min) + min).toInt()
        if (newValue % step == 0) {
            onValueChange(newValue)
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .onSizeChanged {
                sliderWidth = it.width
            }
            // 处理拖动事件
            .pointerInput(sliderWidth, step, min, max) {
                detectHorizontalDragGestures { change, _ ->
                    handleChange(change.position.x)
                }
            }
            // 处理点击事件
            .pointerInput(Unit) {
                detectTapGestures {
                    handleChange(it.x)
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        // 滑轨
        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(BorderColor),
            contentAlignment = Alignment.CenterStart
        ) {
            // 高亮线段
            Box(
                Modifier
                    .fillMaxWidth(fraction)
                    .height(2.dp)
                    .background(PrimaryColor)
            )
        }
        // 手柄
        Box(
            Modifier
                .size(28.dp)
                .offset(offsetX - 14.dp)
                .shadow(14.dp, CircleShape, spotColor = BorderColor)
                .background(Color.White, CircleShape)
        )

    }
}
