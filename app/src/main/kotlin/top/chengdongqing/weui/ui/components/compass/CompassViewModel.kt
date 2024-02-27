package top.chengdongqing.weui.ui.components.compass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CompassViewModel : ViewModel() {
    var degrees by mutableIntStateOf(0)
        private set
    var accuracy by mutableStateOf<Int?>(null)
        private set
    var observing by mutableStateOf(false)

    val eventListener by lazy {
        CompassEventListener(
            onDegreesChange = {
                degrees = it
            },
            onAccuracyChange = {
                accuracy = it
            }
        )
    }
}

class CompassEventListener(
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