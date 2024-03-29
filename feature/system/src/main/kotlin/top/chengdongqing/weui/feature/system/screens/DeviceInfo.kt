package top.chengdongqing.weui.feature.system.screens

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.res.Configuration
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.nfc.NfcManager
import android.os.Build
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import top.chengdongqing.weui.core.ui.components.cardlist.WeCardListItem
import top.chengdongqing.weui.core.ui.components.cardlist.cartList
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.rememberBatteryInfo
import top.chengdongqing.weui.core.utils.rememberStatusBarHeight

@Composable
fun DeviceInfoScreen() {
    WeScreen(title = "DeviceInfo", description = "设备信息", scrollEnabled = false) {
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
                Pair("充电中", battery.isCharging.format())
            ).apply {
                addScreenItems(context, density, configuration, statusBarHeight)
                addHardwareItems(context)
            }
        }

        LazyColumn(modifier = Modifier.cartList()) {
            items(deviceInfoItems) {
                WeCardListItem(it.first, it.second)
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
    add(Pair("状态栏高度", "${statusBarHeight.value.format()}(dp)"))
}

private fun MutableList<Pair<String, String>>.addHardwareItems(context: Context) {
    (context.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.let {
        add(Pair("WiFi", it.isWifiEnabled.formatEnable()))
    }
    (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter?.let {
        add(Pair("蓝牙", it.isEnabled.formatEnable()))
    }
    (context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager)?.let {
        val isGpsEnabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER)
        add(Pair("GPS", isGpsEnabled.formatEnable()))
    }
    (context.getSystemService(Context.NFC_SERVICE) as? NfcManager)?.defaultAdapter?.let {
        add(Pair("NFC", it.isEnabled.formatEnable()))
    }
}

private fun Boolean.formatEnable() = this.format("开", "关")