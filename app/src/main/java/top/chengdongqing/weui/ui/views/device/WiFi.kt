package top.chengdongqing.weui.ui.views.device

import android.content.Context
import android.net.wifi.WifiManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun WiFiPage() {
    Page(title = "Wi-Fi", description = "无线局域网") {
        val wifiManager = LocalContext.current.getSystemService(Context.WIFI_SERVICE) as WifiManager

        WeButton(text = "扫描Wi-Fi") {

        }
    }
}