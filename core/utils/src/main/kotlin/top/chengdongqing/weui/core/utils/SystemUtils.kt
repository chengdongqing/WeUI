package top.chengdongqing.weui.core.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.data.model.MimeTypes
import java.io.File

@Composable
fun rememberStatusBarHeight(): Dp {
    val density = LocalDensity.current
    val statusBars = WindowInsets.statusBars

    return remember {
        with(density) {
            statusBars.getTop(this).toDp()
        }
    }
}

@Composable
fun rememberBatteryInfo(): BatteryInfo {
    val batteryStatus = LocalContext.current.registerReceiver(
        null,
        IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    )

    val level by remember {
        derivedStateOf {
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0) ?: 0
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, 0) ?: 0
            (level / scale.toFloat() * 100).toInt()
        }
    }
    val isCharging by remember {
        derivedStateOf {
            val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        }
    }

    return BatteryInfo(level, isCharging)
}

data class BatteryInfo(
    val level: Int,
    val isCharging: Boolean
)

/**
 * 显示提示框
 */
fun Context.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

/**
 * 安装APK
 */
fun Context.installApk(apkPath: String) {
    val tempFile = File.createTempFile("app_", ".apk").apply {
        deleteOnExit()
    }
    File(apkPath).copyTo(tempFile, true)
    openFile(tempFile, MimeTypes.APK)
}

/**
 * 寻找Activity
 */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

/**
 * 清除之前产生的所有缓存
 */
fun Context.clearAllCache() {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            // 清理内部缓存 (/data/user/0/包名/cache)
            deleteDirContent(cacheDir)
            // 清理外部缓存 (/sdcard/Android/data/包名/cache)
            deleteDirContent(externalCacheDir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * 删除目录下的内容
 */
private fun deleteDirContent(dir: File?): Boolean {
    return dir != null && if (dir.exists() && dir.isDirectory) {
        dir.listFiles()?.forEach { child ->
            child.deleteRecursively()
        }
        true
    } else {
        false
    }
}