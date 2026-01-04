package top.chengdongqing.weui.feature.hardware.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.cardlist.cardList
import top.chengdongqing.weui.core.ui.components.divider.WeDivider
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.dbmToPercentage
import top.chengdongqing.weui.core.utils.showToast
import top.chengdongqing.weui.feature.hardware.components.DiscoveryLoading

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WiFiScreen() {
    WeScreen(title = "Wi-Fi", description = "无线局域网", scrollEnabled = false) {
        val context = LocalContext.current
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as? WifiManager
        var wifiList by remember { mutableStateOf<List<WiFiInfo>>(emptyList()) }
        val permissionState = rememberMultiplePermissionsState(
            remember {
                listOf(
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
        )
        val (observing, setObserving) = remember { mutableStateOf(false) }

        WeButton(text = "扫描Wi-Fi") {
            if (permissionState.allPermissionsGranted) {
                if (wifiManager == null) {
                    context.showToast("此设备不支持Wi-Fi")
                } else if (wifiManager.isWifiEnabled) {
                    wifiList = wifiManager.scanResults.buildWiFiList()
                    setObserving(true)
                } else {
                    context.showToast("Wi-Fi未开启")
                    context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
            } else {
                permissionState.launchMultiplePermissionRequest()
            }
        }

        if (observing) {
            Spacer(modifier = Modifier.height(40.dp))
            WiFiList(wifiList)
        }
    }
}

@Composable
private fun WiFiList(wifiList: List<WiFiInfo>) {
    if (wifiList.isNotEmpty()) {
        LazyColumn(modifier = Modifier.cardList()) {
            itemsIndexed(wifiList) { index, wifi ->
                WiFiListItem(wifi)
                if (index < wifiList.lastIndex) {
                    WeDivider()
                }
            }
        }
    } else {
        DiscoveryLoading("正在扫描Wi-Fi...")
    }
}

@Composable
private fun WiFiListItem(wifi: WiFiInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (wifi.ssid.isNotEmpty()) {
                    Text(
                        text = wifi.ssid,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(
                    text = wifi.band,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 10.sp,
                    modifier = Modifier
                        .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(3.dp))
                        .padding(horizontal = 3.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = buildString {
                    appendLine("信号强度：${wifi.rssi}dBm （${wifi.percentage}%）")
                    appendLine("MAC地址：${wifi.mac}")
                    appendLine("加密类型：${wifi.security}")
                    appendLine("技术标准：${wifi.generation}")
                    append("中心频率：${wifi.frequency}MHz")
                },
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 10.sp,
                lineHeight = 14.sp
            )
        }

        if (wifi.isProtected) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "加密",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

private fun List<ScanResult>.buildWiFiList(): List<WiFiInfo> {
    return this.sortedByDescending { it.level }
        .map { result ->
            WiFiInfo(
                ssid = getWifiSSID(result),
                mac = result.BSSID,
                frequency = result.frequency,
                rssi = result.level,
                percentage = dbmToPercentage(result.level, minDbm = -90),
                band = getWifiBand(result.frequency),
                isProtected = isWifiProtected(result.capabilities),
                security = getWifiSecurity(result.capabilities),
                generation = getWifiGeneration(result)
            )
        }
}

/**
 * 获取Wi-Fi名称
 */
private fun getWifiSSID(wifi: ScanResult): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        wifi.wifiSsid?.bytes?.decodeToString() ?: ""
    } else {
        @Suppress("DEPRECATION")
        wifi.SSID
    }
}

/**
 * 获取Wi-Fi频段
 */
private fun getWifiBand(frequency: Int): String {
    return when (frequency) {
        in 2400..2500 -> "2.4G"
        in 4900..5900 -> "5G"
        in 5925..7125 -> "6G"
        else -> "未知频段"
    }
}

/**
 * 判断是否加密
 */
private fun isWifiProtected(capabilities: String): Boolean {
    return capabilities.contains("WEP") || capabilities.contains("PSK") || capabilities.contains("EAP")
}

/**
 * 获取Wi-Fi安全性
 */
private fun getWifiSecurity(capabilities: String?): String {
    if (capabilities == null) return "未知"
    val cap = capabilities.uppercase()

    return when {
        // WPA3 系列
        cap.contains("SUITE-B-192") -> "WPA3-Enterprise (192-bit)"
        cap.contains("SAE") && cap.contains("EAP") -> "WPA3-Enterprise"
        cap.contains("SAE") -> "WPA3-Personal"
        // WPA2 系列
        cap.contains("EAP") && cap.contains("WPA2") -> "WPA2-Enterprise"
        cap.contains("PSK") && cap.contains("WPA2") -> {
            // 很多设备在过渡期会显示为 WPA2/WPA3
            if (cap.contains("SAE")) "WPA2/WPA3-Personal" else "WPA2-Personal"
        }
        // 旧协议
        cap.contains("WEP") -> "WEP"
        // 开放
        !cap.contains("WPA") && !cap.contains("WEP") && !cap.contains("EAP") -> "开放"
        else -> "加密"
    }
}

/**
 * 获取技术标准
 */
private fun getWifiGeneration(scanResult: ScanResult): String {
    // 优先使用官方 API (Android 11+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        return when (scanResult.wifiStandard) {
            ScanResult.WIFI_STANDARD_LEGACY -> "Wi-Fi 1/2/3"
            ScanResult.WIFI_STANDARD_11N -> "Wi-Fi 4"
            ScanResult.WIFI_STANDARD_11AC -> "Wi-Fi 5"
            ScanResult.WIFI_STANDARD_11AX -> "Wi-Fi 6"
            // Android 14+ 支持 Wi-Fi 7
            8 -> "Wi-Fi 7"
            else -> "Wi-Fi"
        }
    }

    // 兼容旧版本 (Android 10 及以下) - 通过特征字符串推断
    val cap = scanResult.capabilities.uppercase()
    return when {
        // VHT = Very High Throughput (802.11ac / Wi-Fi 5)
        cap.contains("VHT") -> "Wi-Fi 5"
        // HT = High Throughput (802.11n / Wi-Fi 4)
        cap.contains("HT") -> "Wi-Fi 4"
        // 5GHz 频段即便没识别出 HT，大概率也是 Wi-Fi 4 以上
        scanResult.frequency > 4900 -> "Wi-Fi 4/5"
        else -> "Wi-Fi 3"
    }
}

private data class WiFiInfo(
    val ssid: String,       // Wi-Fi名称
    val mac: String,        // MAC 地址
    val frequency: Int,     // 频率 (MHz)
    val rssi: Int,          // 信号强度 (dBm)
    val percentage: Int,    // 信号强度（百分比）
    val band: String,       // 频段
    val isProtected: Boolean, // 是否加密
    val security: String,    // 安全性
    val generation: String   // 技术标准
)
