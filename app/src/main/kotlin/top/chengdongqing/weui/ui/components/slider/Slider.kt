package top.chengdongqing.weui.ui.components.slider

import androidx.annotation.IntRange
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.PrimaryColor

/**
 * 滑块
 *
 * @param value 当前值
 * @param valueRange 可选值区间
 * @param step 步长
 * @param disabled 是否禁用
 * @param formatter 值格式化函数
 * @param onChangeFinished 值结束变化事件
 * @param onChange 值改变事件
 */
@Composable
fun WeSlider(
    value: Float,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
    @IntRange(from = 0)
    step: Int = 1,
    disabled: Boolean = false,
    formatter: ((Float) -> String)? = null,
    onChangeFinished: (() -> Unit)? = null,
    onChange: (Float) -> Unit
) {
    val min = valueRange.start
    val max = valueRange.endInclusive
    val density = LocalDensity.current
    var sliderWidth by remember { mutableIntStateOf(0) }
    var percent by remember { mutableFloatStateOf(0f) }
    var offsetX by remember { mutableStateOf(0.dp) }

    // 值改变后计算相应元素的位置或宽度
    LaunchedEffect(value, sliderWidth) {
        percent = (value.coerceIn(min, max) - min) / (max - min)
        offsetX = density.run { (sliderWidth * percent).toDp() }
    }

    // 处理滑动或点击事件
    fun handleChange(offsetX: Float) {
        if (!disabled) {
            val newFraction = (offsetX / sliderWidth).coerceIn(0f, 1f)
            val newValue = newFraction * (max - min) + min
            if (newValue.toInt() % step == 0) {
                onChange(newValue)
            }
        }
    }

    Row(
        modifier = Modifier
            .height(48.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .onSizeChanged {
                    sliderWidth = it.width
                }
                // 处理拖动事件
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(onDragEnd = {
                        onChangeFinished?.invoke()
                    }) { change, _ ->
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
                    .background(MaterialTheme.colorScheme.outline),
                contentAlignment = Alignment.CenterStart
            ) {
                // 高亮线段
                Box(
                    Modifier
                        .fillMaxWidth(percent)
                        .height(2.dp)
                        .background(PrimaryColor)
                )
            }
            // 手柄
            Box(
                Modifier
                    .size(28.dp)
                    .offset(offsetX - 14.dp)
                    .shadow(14.dp, CircleShape, spotColor = MaterialTheme.colorScheme.outline)
                    .background(Color.White, CircleShape)
            )
        }

        formatter?.let {
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = it(value),
                modifier = Modifier.widthIn(40.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.End
            )
        }
    }
}