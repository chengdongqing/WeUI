package top.chengdongqing.weui.ui.screens.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.slider.WeSlider
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun SliderScreen() {
    WeScreen(title = "Slider", description = "滑块") {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var value by remember { mutableFloatStateOf(0f) }
            WeSlider(
                value,
                formatter = { "${formatFloat(it, 0)}%" }
            ) {
                value = it
            }

            Spacer(modifier = Modifier.height(20.dp))

            var value1 by remember { mutableFloatStateOf(0f) }
            KvRow(key = "定义可选值区间", value = formatFloat(value1))
            WeSlider(
                value = value1,
                valueRange = -999.99f..999.99f
            ) {
                value1 = it
            }

            Spacer(modifier = Modifier.height(20.dp))

            var value2 by remember { mutableFloatStateOf(0f) }
            var value2String by remember { mutableStateOf("0") }
            KvRow(key = "滑动结束后触发", value = value2String)
            WeSlider(
                value = value2,
                valueRange = 0f..1f,
                onChangeFinished = {
                    value2String = formatFloat(value2)
                }
            ) {
                value2 = it
            }
        }
    }
}

@Composable
fun KvRow(key: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = key,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 14.sp
        )
    }
}