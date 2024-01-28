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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import top.chengdongqing.weui.ui.components.basic.KeyValueCard
import top.chengdongqing.weui.ui.components.basic.KeyValueRow
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun DeviceInfoPage() {
    WePage(title = "DeviceInfo", description = "设备信息") {
        val context = LocalContext.current

        KeyValueCard {
            item {
                KeyValueRow("设备品牌", Build.BRAND)
                KeyValueRow("设备型号", Build.MODEL)
                KeyValueRow("设备像素比", LocalDensity.current.density.toString())
                val displayMetrics = context.resources.displayMetrics
                KeyValueRow(
                    "屏幕分辨率",
                    "${displayMetrics.widthPixels}x${displayMetrics.heightPixels}(px)"
                )
                val configuration = LocalConfiguration.current
                KeyValueRow(
                    "屏幕宽高",
                    "${configuration.screenWidthDp}x${configuration.screenHeightDp}(dp)"
                )
                StatusBarHeightRow()
                KeyValueRow("系统语言", LocalConfiguration.current.locales.toLanguageTags())
                KeyValueRow("字体缩放比例", LocalConfiguration.current.fontScale.toString())
                KeyValueRow("系统版本", "Android ${Build.VERSION.RELEASE}")
                (context.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.let {
                    KeyValueRow("WiFi开关", formatEnableStatus(it.isWifiEnabled))
                }
                (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.let {
                    KeyValueRow("蓝牙开关", formatEnableStatus(it.adapter.isEnabled))
                }
                (context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager)?.let {
                    val isGpsEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    KeyValueRow("GPS开关", formatEnableStatus(isGpsEnabled))
                }
                (context.getSystemService(Context.NFC_SERVICE) as? NfcManager)?.let {
                    val isNfcEnabled = it.defaultAdapter?.isEnabled == true
                    KeyValueRow("NFC开关", formatEnableStatus(isNfcEnabled))
                }
                BatteryRows(context)
                ScreenOrientationRow()
            }
        }
    }
}

@Composable
private fun StatusBarHeightRow() {
    val view = LocalView.current
    val density = LocalDensity.current
    val statusBarHeight = remember {
        val height = ViewCompat.getRootWindowInsets(view)
            ?.getInsets(WindowInsetsCompat.Type.statusBars())?.top ?: 0
        with(density) {
            height.toDp()
        }
    }
    KeyValueRow("状态栏高度", "${formatFloat(statusBarHeight.value)}(dp)")
}

@Composable
private fun BatteryRows(context: Context) {
    val batteryStatus = context.registerReceiver(
        null,
        IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    )
    val batteryPercent by remember {
        derivedStateOf {
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            level / scale * 100f
        }
    }
    val isCharging by remember {
        derivedStateOf {
            val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        }
    }
    KeyValueRow("电量", "${formatFloat(batteryPercent)}%")
    KeyValueRow("充电中", if (isCharging) "是" else "否")
}

@Composable
private fun ScreenOrientationRow() {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    KeyValueRow("屏幕方向", if (isLandscape) "横屏" else "竖屏")
}

private fun formatEnableStatus(value: Boolean): String {
    return if (value) "开" else "关"
}
