package top.chengdongqing.weui.ui.views.hardware.compass

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.page.WePage
import top.chengdongqing.weui.ui.theme.FontColor1
import top.chengdongqing.weui.ui.views.hardware.sensor.rememberSensorValue
import top.chengdongqing.weui.utils.formatFloat

/**
 * 基于磁力计和加速度计实现的原因：
 * 1. 磁力计：这种传感器可以检测地球磁场的强度和方向。由于地球本身是一个巨大的磁体，磁力计可以用来确定手机相对于地球磁北极的方向。这是指南针功能的核心部分，因为它提供了指向地理北极（根据地球磁场）的基础方向信息。
 * 2. 加速度计：虽然磁力计能提供方向信息，但它本身无法确定手机的倾斜状态（即手机是水平放置还是倾斜）。加速度计可以检测手机相对于地心引力的加速度，从而帮助判断手机的倾斜角度或方位。这对于调整指南针读数以适应用户在不同姿势（如站立、坐着或躺着）下使用手机时是非常重要的。
 * 结合这两种传感器，手机能够提供更准确的方向信息。当你移动手机时，磁力计和加速度计的数据会被实时处理，以确保指南针指示的方向尽可能准确，无论设备的放置方式如何。这种组合使得手机上的指南针功能既实用又灵活，能够满足日常生活中对方向判断的需求。
 */
@Composable
fun CompassPage() {
    WePage(title = "Compass", description = "罗盘（指南针），基于磁力计与加速度计") {
        val (observing, setObserving) = remember { mutableStateOf(false) }
        val pressure = rememberSensorValue(Sensor.TYPE_PRESSURE, observing)
        val (degrees, accuracy) = rememberDegrees(observing)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Compass(degrees)
            Spacer(modifier = Modifier.height(20.dp))
            accuracy?.let {
                Text(text = "精度：${determineAccuracy(it)}", color = FontColor1, fontSize = 10.sp)
            }
            pressure?.let {
                Text(
                    text = "气压：${formatFloat(pressure)}hPa（百帕斯卡）",
                    color = FontColor1,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
            if (!observing) {
                WeButton(text = "开始监听") {
                    setObserving(true)
                }
            } else {
                WeButton(text = "取消监听", type = ButtonType.PLAIN) {
                    setObserving(false)
                }
            }
        }
    }
}

@Composable
private fun rememberDegrees(observing: Boolean): Pair<Int, Int?> {
    val (degrees, setDegrees) = remember { mutableIntStateOf(0) }
    val (accuracy, setAccuracy) = remember { mutableStateOf<Int?>(null) }

    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    // 加速度计
    val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    // 磁力计
    val magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    val eventListener = remember {
        CompassListener(
            onDegreesChange = {
                setDegrees(it)
            },
            onAccuracyChange = {
                setAccuracy(it)
            }
        )
    }

    LaunchedEffect(observing) {
        if (observing) {
            sensorManager.registerListener(
                eventListener,
                accelerometerSensor,
                SensorManager.SENSOR_DELAY_UI
            )
            sensorManager.registerListener(
                eventListener,
                magnetometerSensor,
                SensorManager.SENSOR_DELAY_UI
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

    return Pair(degrees, accuracy)
}

private class CompassListener(
    val onDegreesChange: (Int) -> Unit,
    val onAccuracyChange: (Int) -> Unit
) : SensorEventListener {
    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> gravity = event.values.clone()
            Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = event.values.clone()
        }

        if (gravity != null && geomagnetic != null) {
            val r = FloatArray(9)
            val i = FloatArray(9)
            val success = SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                var azimuth = Math.toDegrees(orientation.first().toDouble())
                if (azimuth < 0) {
                    azimuth += 360
                }
                onDegreesChange(azimuth.toInt())
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        if (sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            onAccuracyChange(accuracy)
        }
    }
}

private fun determineAccuracy(accuracy: Int): String? {
    return when (accuracy) {
        SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "高精度"
        SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "中精度"
        SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "低精度"
        else -> null
    }
}