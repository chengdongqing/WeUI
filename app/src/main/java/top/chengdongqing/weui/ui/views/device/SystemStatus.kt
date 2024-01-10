package top.chengdongqing.weui.ui.views.device

import android.content.Context
import android.net.ConnectivityManager
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.ui.components.Page

@Composable
fun SystemStatusPage() {
    Page(title = "SystemStatus", description = "系统状态") {
        val connectivityManager =
            LocalContext.current.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        val isConnected = networkInfo?.isConnected == true
        val networkType = networkInfo?.type

        Column {
            Text(text = "网络状态")
            Text(text = "截屏事件")
        }
    }
}