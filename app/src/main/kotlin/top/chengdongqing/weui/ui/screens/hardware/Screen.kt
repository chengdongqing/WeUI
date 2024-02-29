package top.chengdongqing.weui.ui.screens.hardware

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.components.slider.WeSlider
import top.chengdongqing.weui.utils.rememberSensorValue

@Composable
fun ScreenScreen() {
    WeScreen(title = "Screen", description = "屏幕") {
        val context = LocalContext.current
        val window = (context as Activity).window
        var screenBrightness by rememberScreenBrightness(window)
        val lightBrightness = rememberSensorValue(Sensor.TYPE_LIGHT, true)

        Box(modifier = Modifier.padding(horizontal = 12.dp)) {
            WeSlider(
                value = (screenBrightness * 100).toInt(),
                onChange = {
                    screenBrightness = it / 100f
                    setScreenBrightness(window, screenBrightness)
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "光线强度：${lightBrightness} Lux（勒克斯）",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.height(40.dp))
        KeepScreenOn(window)
        Spacer(modifier = Modifier.height(20.dp))
        DisabledScreenshot(window)
    }
}

@Composable
private fun KeepScreenOn(window: Window) {
    var value by remember { mutableStateOf(false) }

    WeButton(text = "${if (value) "取消" else "保持"}屏幕常亮") {
        value = !value
        if (value) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Composable
private fun DisabledScreenshot(window: Window) {
    var value by remember { mutableStateOf(false) }

    WeButton(
        text = "${if (value) "取消" else "开启"}禁止截屏",
        type = ButtonType.PLAIN
    ) {
        value = !value
        if (value) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }
}

@Composable
private fun rememberScreenBrightness(window: Window): MutableState<Float> {
    val context = LocalContext.current
    val brightness = remember { mutableFloatStateOf(getScreenBrightness(context)) }

    DisposableEffect(Unit) {
        val initialValue = brightness.floatValue

        onDispose {
            setScreenBrightness(window, initialValue)
        }
    }

    return brightness
}

private fun getScreenBrightness(context: Context): Float {
    val brightness = Settings.System.getInt(
        context.contentResolver,
        Settings.System.SCREEN_BRIGHTNESS
    )
    return brightness / 255f * 2
}

private fun setScreenBrightness(window: Window, brightness: Float) {
    window.attributes = window.attributes.apply {
        screenBrightness = if (brightness > 1 || brightness < 0) -1f else brightness
    }
}
