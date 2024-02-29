package top.chengdongqing.weui.ui.screens.system

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.pairgroup.WePairGroup
import top.chengdongqing.weui.ui.components.pairgroup.WePairItem
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.utils.SetupStatusBarStyle

@Composable
fun SystemStatusScreen() {
    WeScreen(title = "SystemStatus", description = "系统状态，动态更新", scrollEnabled = false) {
        WePairGroup {
            item {
                NetworkInfoRows()
                WePairItem("系统主题", if (isSystemInDarkTheme()) "深色" else "浅色")
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        StatusBarAction()
    }
}

@Composable
private fun NetworkInfoRows() {
    val network = rememberNetworkObserver()

    WePairItem("网络类型", network.type)
    WePairItem("VPN", if (network.isVpnConnected) "已启用" else "未启用")
}

@Composable
private fun rememberNetworkObserver(): NetworkInfo {
    val context = LocalContext.current
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    var networkType by remember { mutableStateOf(getNetworkType(connectivityManager)) }
    var isVpnConnected by remember { mutableStateOf(isVpnConnected(connectivityManager)) }

    DisposableEffect(Unit) {
        val callbackHandler = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                getNetworkStatus()
            }

            override fun onLost(network: Network) {
                getNetworkStatus()
            }

            private fun getNetworkStatus() {
                networkType = getNetworkType(connectivityManager)
                isVpnConnected = isVpnConnected(connectivityManager)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callbackHandler)

        onDispose {
            connectivityManager.unregisterNetworkCallback(callbackHandler)
        }
    }

    return NetworkInfo(networkType, isVpnConnected)
}

@Composable
private fun StatusBarAction() {
    var isDark by remember { mutableStateOf(true) }

    SetupStatusBarStyle(isDark)
    WeButton(text = "切换状态栏样式", type = ButtonType.PLAIN) {
        isDark = !isDark
    }
}

private data class NetworkInfo(
    val type: String,
    val isVpnConnected: Boolean
)

private fun getNetworkType(connectivityManager: ConnectivityManager): String {
    val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

    return capabilities?.let {
        when {
            it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "移动网络"
            it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "蓝牙"
            it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "以太网"
            it.hasTransport(NetworkCapabilities.TRANSPORT_USB) -> "USB"
            else -> "未知网络"
        }
    } ?: "网络未连接"
}

private fun isVpnConnected(connectivityManager: ConnectivityManager): Boolean {
    return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        ?.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        ?: false
}