package top.chengdongqing.weui.ui.views.hardware

import android.hardware.Sensor
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.views.hardware.sensor.rememberSensorValues
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun GyroscopePage() {
    WePage(title = "Gyroscope", description = "陀螺仪，用于测量旋转运动") {
        val (observing, setObserving) = remember { mutableStateOf(false) }
        val values = rememberSensorValues(Sensor.TYPE_GYROSCOPE, observing)

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

            values?.let {
                Text(text = "单位：rad/s（弧度/秒）", fontSize = 10.sp)
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
}

private fun getAxisLabel(index: Int): String {
    return when (index) {
        0 -> "X"
        1 -> "Y"
        2 -> "Z"
        else -> ""
    }
}