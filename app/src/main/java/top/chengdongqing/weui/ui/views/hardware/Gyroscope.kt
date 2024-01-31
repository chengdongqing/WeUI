package top.chengdongqing.weui.ui.views.hardware

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.basic.KeyValueCard
import top.chengdongqing.weui.ui.components.basic.KeyValueRow
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun GyroscopePage() {
    WePage(title = "Gyroscope", description = "陀螺仪，用于测量旋转运动") {
        val (observing, setObserving) = remember { mutableStateOf(false) }
        val values = rememberGyroscope(observing)

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
                KeyValueCard {
                    itemsIndexed(it) { index, value ->
                        KeyValueRow(
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

@Composable
private fun rememberGyroscope(observing: Boolean): Array<Float>? {
    val (values, setValues) = remember {
        mutableStateOf<Array<Float>?>(null)
    }

    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    val eventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    setValues(it.values.toTypedArray())
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    LaunchedEffect(observing) {
        if (observing) {
            sensorManager.registerListener(
                eventListener,
                gyroscopeSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } else {
            sensorManager.unregisterListener(eventListener)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            sensorManager.unregisterListener(eventListener)
        }
    }

    return values
}