package top.chengdongqing.weui.ui.views.hardware

import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeSlider

@Composable
fun ScreenPage() {
    Page(title = "Screen", description = "屏幕") {
        val context = LocalContext.current
        val window = (context as Activity).window
        var brightness by remember {
            mutableFloatStateOf(getScreenBrightness(context))
        }

        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeSlider(
                value = (brightness * 100).toInt(),
                onChange = {
                    brightness = it / 100f
                    setScreenBrightness(window, brightness)
                }
            )
            Spacer(modifier = Modifier.height(40.dp))

            var keepScreenOn by remember {
                mutableStateOf(false)
            }
            WeButton(text = "${if (keepScreenOn) "取消" else "保持"}屏幕常亮") {
                keepScreenOn = !keepScreenOn
                if (keepScreenOn) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            var screenshotDisabled by remember {
                mutableStateOf(false)
            }
            WeButton(
                text = "${if (screenshotDisabled) "取消" else "开启"}禁止截屏",
                type = ButtonType.PLAIN
            ) {
                screenshotDisabled = !screenshotDisabled
                if (screenshotDisabled) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }
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
