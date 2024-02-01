package top.chengdongqing.weui.ui.views.hardware.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberSensorValue(type: Int, observing: Boolean): Float? {
    return rememberSensorValues(type, observing)?.let { it[0] }
}

@Composable
fun rememberSensorValues(type: Int, observing: Boolean): Array<Float>? {
    val (values, setValues) = remember {
        mutableStateOf<Array<Float>?>(null)
    }

    val context = LocalContext.current
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val sensor = sensorManager.getDefaultSensor(type)

    if (sensor != null) {
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
                    sensor,
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
    }

    return values
}