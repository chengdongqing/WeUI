package top.chengdongqing.weui.ui.views.hardware

import android.hardware.Sensor
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.basic.KeyValueCard
import top.chengdongqing.weui.ui.components.basic.KeyValueRow
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.views.hardware.sensor.rememberSensorValue

@Composable
fun HygrothermographPage() {
    WePage(title = "Hygrothermograph", description = "温湿度计") {
        val (observing, setObserving) = remember { mutableStateOf(false) }
        val temperature = rememberSensorValue(Sensor.TYPE_AMBIENT_TEMPERATURE, observing)
        val humidity = rememberSensorValue(Sensor.TYPE_RELATIVE_HUMIDITY, observing)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (!observing) {
                WeButton(text = "开始监听") {
                    setObserving(true)
                }
            } else {
                WeButton(text = "取消监听", type = ButtonType.PLAIN) {
                    setObserving(false)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            KeyValueCard {
                item {
                    KeyValueRow(
                        label = "温度",
                        value = temperature?.let { "${temperature}°C" } ?: "未知"
                    )
                    KeyValueRow(
                        label = "湿度",
                        value = humidity?.let { "${humidity}%" } ?: "未知")
                }
            }
        }
    }
}