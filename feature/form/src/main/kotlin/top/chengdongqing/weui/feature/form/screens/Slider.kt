package top.chengdongqing.weui.feature.form.screens

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
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.slider.WeSlider
import top.chengdongqing.weui.core.utils.format

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
                formatter = { "${it.format(0)}%" }
            ) {
                value = it
            }

            Spacer(modifier = Modifier.height(20.dp))

            var value1 by remember { mutableFloatStateOf(0f) }
            KvRow(key = "定义可选值区间", value = value1.format())
            WeSlider(
                value = value1,
                range = -999.99f..999.99f
            ) {
                value1 = it
            }

            Spacer(modifier = Modifier.height(20.dp))

            var value2 by remember { mutableFloatStateOf(0f) }
            var value2String by remember { mutableStateOf("0") }
            KvRow(key = "滑动结束后触发", value = value2String)
            WeSlider(
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