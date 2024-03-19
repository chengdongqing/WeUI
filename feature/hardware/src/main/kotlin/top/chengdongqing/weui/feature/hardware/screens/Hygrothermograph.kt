package top.chengdongqing.weui.feature.hardware.screens

import android.hardware.Sensor
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardList
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.feature.hardware.utils.rememberSensorValue

@Composable
fun HygrothermographScreen() {
    WeScreen(title = "Hygrothermograph", description = "温湿度计", scrollEnabled = false) {
        val (observing, setObserving) = remember { mutableStateOf(false) }
        val temperature = rememberSensorValue(Sensor.TYPE_AMBIENT_TEMPERATURE, observing)
        val humidity = rememberSensorValue(Sensor.TYPE_RELATIVE_HUMIDITY, observing)

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
        WeCardList {
            item {
                WeCardListItem(
                    label = "温度",
                    value = temperature?.let { "${temperature.format()}°C" } ?: "未知"
                )
                WeCardListItem(
                    label = "湿度",
                    value = humidity?.let { "${humidity.format()}%" } ?: "未知")
            }
        }
    }
}
