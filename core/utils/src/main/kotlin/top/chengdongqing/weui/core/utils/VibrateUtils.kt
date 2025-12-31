package top.chengdongqing.weui.core.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * 触发短震动
 */
fun Context.vibrateShort() {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        }

        else -> vibrateRaw(15, 50) // 老设备降级，并调低振幅
    }
}

/**
 * 触发长震动
 */
fun Context.vibrateLong() {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            // 前100ms较强，后300ms平缓减弱
            val timings = longArrayOf(100, 300)
            val amplitudes = intArrayOf(100, 30)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        }

        else -> vibrateRaw(400, 80)
    }
}

/**
 * 底层震动实现
 */
private fun Context.vibrateRaw(
    milliseconds: Long,
    amplitude: Int = VibrationEffect.DEFAULT_AMPLITUDE
) {
    val v = vibrator
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            v.vibrate(VibrationEffect.createOneShot(milliseconds, amplitude))
        }

        else -> {
            @Suppress("DEPRECATION")
            v.vibrate(milliseconds)
        }
    }
}

/**
 * 获取当前设备可用的振动器（适配 API 31+）
 */
private val Context.vibrator: Vibrator
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }