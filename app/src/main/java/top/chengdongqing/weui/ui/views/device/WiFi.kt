package top.chengdongqing.weui.ui.views.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
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
import androidx.compose.material3.Divider
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
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.basic.LoadMoreType
import top.chengdongqing.weui.ui.components.basic.WeLoadMore
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.theme.BorderColor
import top.chengdongqing.weui.ui.theme.FontColo1
import top.chengdongqing.weui.ui.theme.FontColor
import top.chengdongqing.weui.ui.theme.LightColor

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WiFiPage() {
    Page(title = "Wi-Fi", description = "无线局域网") {
        val wifiManager = LocalContext.current.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val permissionState =
            rememberPermissionState(permission = Manifest.permission.ACCESS_FINE_LOCATION)
        var wifiList by remember {
            mutableStateOf<List<ScanResult>>(emptyList())
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeButton(text = "扫描Wi-Fi") {
                if (permissionState.status.isGranted) {
                    wifiList = wifiManager.scanResults.sortedByDescending { it.level }
                } else {
                    permissionState.launchPermissionRequest()
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            if (wifiList.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp)
                ) {
                    itemsIndexed(wifiList) { index, wifi ->
                        WifiItem(wifi)
                        if (index < wifiList.size - 1) {
                            Divider(thickness = 0.5.dp, color = BorderColor)
                        }
                    }
                }
            } else {
                WeLoadMore(type = LoadMoreType.ALL_LOADED)
            }
        }
    }
}

@Composable
private fun WifiItem(wifi: ScanResult) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = getSSID(wifi))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = determineWifiBand(wifi.frequency),
                color = FontColo1,
                fontSize = 10.sp,
                modifier = Modifier
                    .border(0.5.dp, BorderColor, RoundedCornerShape(3.dp))
                    .padding(horizontal = 3.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isWifiSecure(wifi.capabilities)) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "加密",
                    modifier = Modifier.size(20.dp),
                    tint = LightColor
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${calculateSignalLevel(wifi.level, 100)}%",
                color = FontColor,
                fontSize = 14.sp
            )
        }
    }
}

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
        in 2400..2500 -> "2.4GHz"
        in 4900..5900 -> "5GHz"
        in 5925..7125 -> "6GHz"
        else -> "未知频段"
    }
}

private fun calculateSignalLevel(rssi: Int, numLevels: Int = 5): Int {
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