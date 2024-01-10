package top.chengdongqing.weui.ui.views.device

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
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.utils.formatFloat

@Composable
fun DeviceInfoPage() {
    Page(title = "DeviceInfo", description = "设备信息") {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
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

            (context.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.also {
                KeyValueRow("WiFi开关", getEnableStatus(it.isWifiEnabled))
            }
            (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.also {
                KeyValueRow("蓝牙开关", getEnableStatus(it.adapter.isEnabled))
            }
            (context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager)?.also {
                val isGpsEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)
                KeyValueRow("GPS开关", getEnableStatus(isGpsEnabled))
            }
            (context.getSystemService(Context.NFC_SERVICE) as? NfcManager)?.also {
                val isNfcEnabled = it.defaultAdapter?.isEnabled == true
                KeyValueRow("NFC开关", getEnableStatus(isNfcEnabled))
            }

            val batteryStatus =
                context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            val batteryPct = level / scale.toFloat() * 100
            val isCharging =
                status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
            KeyValueRow("当前电量", "${formatFloat(batteryPct)}%")
            KeyValueRow("是否充电中", if (isCharging) "是" else "否")
            KeyValueRow("系统主题", if (isSystemInDarkTheme()) "深色" else "浅色")
            val orientation =
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) "横屏" else "竖屏"
            KeyValueRow("屏幕方向", orientation)
        }
    }
}

private fun getEnableStatus(value: Boolean): String {
    return if (value) "开" else "关"
}

@Composable
fun KeyValueRow(label: String, value: String) {
    Row(
        modifier = Modifier.height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = FontColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value,
            modifier = Modifier.weight(1f),
            color = FontColo1,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Divider(thickness = 0.5.dp, color = BorderColor)
}