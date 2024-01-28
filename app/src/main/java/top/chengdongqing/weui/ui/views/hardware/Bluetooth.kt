package top.chengdongqing.weui.ui.views.hardware

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun BluetoothPage() {
    WePage(title = "Bluetooth", description = "蓝牙") {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeButton(text = "扫描蓝牙") {

            }
        }
    }
}

private data class Bluetooth(
    val name: String?,
    val address: String,
    val rssi: Int,
    val uuids: List<String>,
    val serviceCount: Int
)