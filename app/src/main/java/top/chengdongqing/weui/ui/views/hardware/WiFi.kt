package top.chengdongqing.weui.ui.views.hardware

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import top.chengdongqing.weui.ui.components.basic.LoadMoreType
import top.chengdongqing.weui.ui.components.basic.WeDivider
import top.chengdongqing.weui.ui.components.basic.WeLoadMore
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.LightColor

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WiFiPage() {
    WePage(title = "Wi-Fi", description = "无线局域网") {
        val context = LocalContext.current
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var wifiList by remember { mutableStateOf<List<WiFi>>(emptyList()) }
        val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeButton(text = "扫描Wi-Fi") {
                if (permissionState.status.isGranted) {
                    if (wifiManager.isWifiEnabled) {
                        wifiList = buildWiFiList(wifiManager.scanResults)
                    } else {
                        Toast.makeText(context, "Wi-Fi未打开", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            WiFiList(wifiList)
        }
    }
}

@Composable
private fun WiFiList(wifiList: List<WiFi>) {
    if (wifiList.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp)
        ) {
            itemsIndexed(wifiList) { index, wifi ->
                WiFiListItem(wifi)
                if (index < wifiList.lastIndex) {
                    WeDivider()
                }
            }
        }
    } else {
        WeLoadMore(type = LoadMoreType.ALL_LOADED)
    }
}

@Composable
private fun WiFiListItem(wifi: WiFi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = wifi.name)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = wifi.band,
                color = FontColo1,
                fontSize = 10.sp,
                modifier = Modifier
                    .border(0.5.dp, BorderColor, RoundedCornerShape(3.dp))
                    .padding(horizontal = 3.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (wifi.secure) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "加密",
                    modifier = Modifier.size(20.dp),
                    tint = LightColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = wifi.level,
                color = FontColor,
                fontSize = 14.sp
            )
        }
    }
}

private fun buildWiFiList(scanResultList: List<ScanResult>): List<WiFi> {
    return scanResultList.sortedByDescending { it.level }
        .map { item ->
            WiFi(
                name = getSSID(item),
                band = determineWifiBand(item.frequency),
                level = "${calculateSignalLevel(item.level)}%",
                secure = isWifiSecure(item.capabilities)
            )
        }
}

private data class WiFi(
    val name: String,
    val band: String,
    val level: String,
    val secure: Boolean
)

private fun getSSID(wifi: ScanResult): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        wifi.wifiSsid?.bytes?.decodeToString() ?: ""
    } else {
        @Suppress("DEPRECATION")
        wifi.SSID
    }
}

private fun determineWifiBand(frequency: Int): String {
    return when (frequency) {
        in 2400..2500 -> "2.4G"
        in 4900..5900 -> "5G"
        in 5925..7125 -> "6G"
        else -> "未知频段"
    }
}

private fun calculateSignalLevel(rssi: Int, numLevels: Int = 100): Int {
    val minRssi = -100
    val maxRssi = -55
    if (rssi <= minRssi) {
        return 0
    } else if (rssi >= maxRssi) {
        return numLevels - 1
    } else {
        val inputRange: Float = (maxRssi - minRssi).toFloat()
        val outputRange = (numLevels - 1).toFloat()
        return ((rssi - minRssi).toFloat() * outputRange / inputRange).toInt()
    }
}

private fun isWifiSecure(capabilities: String): Boolean {
    return capabilities.contains("WEP") || capabilities.contains("PSK") || capabilities.contains("EAP")
}