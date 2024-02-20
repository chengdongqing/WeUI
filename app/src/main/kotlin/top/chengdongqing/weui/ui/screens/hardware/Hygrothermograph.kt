package top.chengdongqing.weui.ui.screens.hardware

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
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.screens.hardware.sensor.rememberSensorValue
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun HygrothermographScreen() {
    WeScreen(title = "Hygrothermograph", description = "温湿度计") {
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

            WePairGroup {
                item {
                    WePairItem(
                        label = "温度",
                        value = temperature?.let { "${formatFloat(temperature)}°C" } ?: "未知"
                    )
                    WePairItem(
                        label = "湿度",
                        value = humidity?.let { "${formatFloat(humidity)}%" } ?: "未知")
                }
            }
        }
    }
}
