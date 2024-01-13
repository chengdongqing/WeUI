package top.chengdongqing.weui.ui.views.device

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun VibrationPage() {
    Page(title = "Vibration", description = "震动") {
        val context = LocalContext.current

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeButton(text = "短震动") {
                vibrate(context, 15)
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "长震动", type = ButtonType.PLAIN) {
                vibrate(context)
            }
        }
    }
}

fun vibrate(context: Context, milliseconds: Long = 400) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibrator =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibrator.defaultVibrator.vibrate(
            VibrationEffect.createOneShot(
                milliseconds,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(milliseconds)
    }
}