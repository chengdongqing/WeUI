package top.chengdongqing.weui.feature.hardware.screens

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.core.ui.components.cardlist.cardList
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.showToast
import top.chengdongqing.weui.feature.hardware.utils.rememberSensorValue

@Composable
fun HygrothermographScreen() {
    val context = LocalContext.current
    val sensorManager =
        remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }

    WeScreen(title = "Hygrothermograph", description = "温湿度计", scrollEnabled = false) {
        val (observing, setObserving) = remember { mutableStateOf(false) }
        val temperature = rememberSensorValue(Sensor.TYPE_AMBIENT_TEMPERATURE, observing)
        val humidity = rememberSensorValue(Sensor.TYPE_RELATIVE_HUMIDITY, observing)

        if (!observing) {
            WeButton(text = "开始监听") {
                // 判断硬件是否支持
                val hasTemperature =
                    sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null
                val hasHumidity =
                    sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null

                if (hasTemperature && hasHumidity) {
                    setObserving(true)
                } else {
                    context.showToast("此设备不支持环境温湿度传感器")
                }
            }
        } else {
            WeButton(text = "取消监听", type = ButtonType.PLAIN) {
                setObserving(false)
            }
        }

        val hasData = (temperature != null && !temperature.isNaN()) ||
                (humidity != null && !humidity.isNaN())
        if (hasData) {
            Spacer(modifier = Modifier.height(40.dp))
            LazyColumn(modifier = Modifier.cardList()) {
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
}
