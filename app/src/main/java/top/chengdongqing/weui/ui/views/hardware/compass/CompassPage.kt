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
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.views.hardware.sensor.rememberSensorValue
import top.chengdongqing.weui.utils.formatFloat

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
                Text(text = "精度：${determineAccuracy(it)}", color = FontColo1, fontSize = 10.sp)
            }
            pressure?.let {
                Text(
                    text = "气压：${formatFloat(pressure)}hPa（百帕斯卡）",
                    color = FontColo1,
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
                var azimuth = Math.toDegrees(orientation[0].toDouble())
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