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
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import top.chengdongqing.weui.ui.components.KeyValueCard
import top.chengdongqing.weui.ui.components.KeyValueRow
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun DeviceInfoPage() {
    Page(title = "DeviceInfo", description = "设备信息") {
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
                KeyValueRow(
                    "屏幕宽高",
                    "${LocalConfiguration.current.screenWidthDp}x${LocalConfiguration.current.screenHeightDp}(dp)"
                )
                KeyValueRow(
                    "状态栏高度",
                    "${
                        formatFloat(
                            WindowInsets.statusBars.asPaddingValues().calculateTopPadding().value
                        )
                    }(dp)"
                )
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

                val batteryStatus =
                    context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                val batteryPercent = level / scale.toFloat() * 100
                val isCharging =
                    status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
                KeyValueRow("当前电量", "${formatFloat(batteryPercent)}%")
                KeyValueRow("是否充电中", if (isCharging) "是" else "否")
                val orientation =
                    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) "横屏" else "竖屏"
                KeyValueRow("屏幕方向", orientation)
            }
        }
    }
}

private fun formatEnableStatus(value: Boolean): String {
    return if (value) "开" else "关"
}
