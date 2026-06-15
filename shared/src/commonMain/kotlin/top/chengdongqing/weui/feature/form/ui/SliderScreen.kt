package top.chengdongqing.weui.feature.form.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.core.ui.components.WeScreen
import top.chengdongqing.weui.core.ui.components.WeSlider
import top.chengdongqing.weui.core.ui.theme.WeTheme
import top.chengdongqing.weui.util.format

@Composable
fun SliderScreen(onBack: () -> Unit) {
    WeScreen(
        title = "Slider",
        description = "滑块",
        onBack = onBack
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var value by remember { mutableFloatStateOf(0f) }
            DemoSlider(
                value,
                formatter = { "${it.format(0)}%" }
            ) {
                value = it
            }

            Spacer(modifier = Modifier.height(20.dp))

            var value1 by remember { mutableFloatStateOf(0f) }
            KvRow(key = "定义可选值区间", value = value1.format())
            DemoSlider(
                value = value1,
                range = -999.99f..999.99f
            ) {
                value1 = it
            }

            Spacer(modifier = Modifier.height(20.dp))

            var value2 by remember { mutableFloatStateOf(0f) }
            var value2String by remember { mutableStateOf("0") }
            KvRow(key = "滑动结束后触发", value = value2String)
            DemoSlider(
                value = value2,
                range = 0f..1f,
                onChangeFinished = {
                    value2String = value2.format()
                }
            ) {
                value2 = it
            }
        }
    }
}

@Composable
private fun KvRow(key: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = key,
            color = WeTheme.colorScheme.textPrimary,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = WeTheme.colorScheme.textSecondary,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun DemoSlider(
    value: Float,
    range: ClosedFloatingPointRange<Float> = 0f..100f,
    formatter: ((Float) -> String)? = null,
    onChangeFinished: (() -> Unit)? = null,
    onChange: (Float) -> Unit
) {
    WeSlider(
        value,
        range = range,
        formatter = formatter,
        thumbSize = 16.dp,
        activeTrackColor = WeTheme.colorScheme.primary,
        inactiveTrackColor = WeTheme.colorScheme.divider,
        onChange = onChange,
        onChangeFinished = onChangeFinished
    )
}