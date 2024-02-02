package top.chengdongqing.weui.ui.views.system

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.nfc.NfcManager
import android.os.BatteryManager
import android.os.Build
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import top.chengdongqing.weui.ui.components.basic.KeyValueCard
import top.chengdongqing.weui.ui.components.basic.KeyValueRow
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun DeviceInfoPage() {
    WePage(title = "DeviceInfo", description = "设备信息") {
        val context = LocalContext.current
        val density = LocalDensity.current
        val configuration = LocalConfiguration.current
        val statusBarHeight = rememberStatusBarHeight()
        val battery = rememberBatteryInfo()

        val deviceInfoItems = remember {
            mutableListOf(
                Pair("设备品牌", Build.BRAND),
                Pair("设备型号", Build.MODEL),
                Pair("系统版本", "Android ${Build.VERSION.RELEASE}"),
                Pair("系统语言", configuration.locales.toLanguageTags()),
                Pair("字体缩放", configuration.fontScale.toString()),
                Pair("电量", "${battery.level}%"),
                Pair("充电中", if (battery.isCharging) "是" else "否")
            ).apply {
                addScreenItems(context, density, configuration, statusBarHeight)
                addHardwareItems(context)
            }
        }

        KeyValueCard {
            items(deviceInfoItems) {
                KeyValueRow(it.first, it.second)
            }
        }
    }
}

private fun MutableList<Pair<String, String>>.addScreenItems(
    context: Context,
    density: Density,
    configuration: Configuration,
    statusBarHeight: Dp
) {
    add(
        Pair(
            "屏幕宽高",
            "${configuration.screenWidthDp}x${configuration.screenHeightDp}(dp)"
        )
    )
    val displayMetrics = context.resources.displayMetrics
    add(
        Pair(
            "屏幕分辨率",
            "${displayMetrics.widthPixels}x${displayMetrics.heightPixels}(px)"
        )
    )
    add(Pair("屏幕像素比", density.density.toString()))
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    add(Pair("屏幕方向", if (isLandscape) "横屏" else "竖屏"))
    add(Pair("状态栏高度", "${formatFloat(statusBarHeight.value)}(dp)"))
}

private fun MutableList<Pair<String, String>>.addHardwareItems(context: Context) {
    (context.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.let {
        add(Pair("WiFi", formatEnableStatus(it.isWifiEnabled)))
    }
    (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.let {
        add(Pair("蓝牙", formatEnableStatus(it.adapter.isEnabled)))
    }
    (context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager)?.let {
        val isGpsEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)
        add(Pair("GPS", formatEnableStatus(isGpsEnabled)))
    }
    (context.getSystemService(Context.NFC_SERVICE) as? NfcManager)?.let {
        val isNfcEnabled = it.defaultAdapter?.isEnabled == true
        add(Pair("NFC", formatEnableStatus(isNfcEnabled)))
    }
}

@Composable
private fun rememberStatusBarHeight(): Dp {
    val density = LocalDensity.current
    val statusBars = WindowInsets.statusBars

    return remember {
        with(density) {
            statusBars.getTop(this).toDp()
        }
    }
}

private data class BatteryInfo(
    val level: Int,
    val isCharging: Boolean
)

@Composable
private fun rememberBatteryInfo(): BatteryInfo {
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

private fun formatEnableStatus(value: Boolean): String {
    return if (value) "开" else "关"
}
