package top.chengdongqing.weui.ui.views.device

import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.view.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeSlider

@Composable
fun ScreenBrightnessPage() {
    Page(title = "ScreenBrightness", description = "屏幕亮度") {
        val context = LocalContext.current
        val window = (context as Activity).window
        var brightness by remember {
            mutableFloatStateOf(getScreenBrightness(context))
        }

        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(text = "调整屏幕亮度")
            WeSlider(
                value = (brightness * 100).toInt(),
                onChange = {
                    brightness = it / 100f
                    setScreenBrightness(window, brightness)
                }
            )
        }

        DisposableEffect(Unit) {
            val initialValue = brightness

            onDispose {
                setScreenBrightness(window, initialValue)
            }
        }
    }
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
