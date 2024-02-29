package top.chengdongqing.weui.ui.screens.hardware

import android.hardware.Sensor
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.utils.formatFloat
import top.chengdongqing.weui.utils.rememberSensorValues

@Composable
fun AccelerometerScreen() {
    WeScreen(
        title = "Accelerometer",
        description = "加速度计，用于测量加速度，包括重力加速度",
        scrollEnabled = false
    ) {
        val (observing, setObserving) = remember { mutableStateOf(false) }
        val values = rememberSensorValues(Sensor.TYPE_ACCELEROMETER, observing)

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
        values?.let {
            Text(
                text = "单位：m/s²（米/秒平方）",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            WePairGroup {
                itemsIndexed(it) { index, value ->
                    WePairItem(
                        label = "${getAxisLabel(index)}轴",
                        value = formatFloat(value)
                    )
                }
            }
        }
    }
}

private fun getAxisLabel(index: Int): String {
    return when (index) {
        0 -> "X"
        1 -> "Y"
        2 -> "Z"
        else -> ""
    }
}