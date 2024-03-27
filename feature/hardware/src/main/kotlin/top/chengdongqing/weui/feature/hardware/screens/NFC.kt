package top.chengdongqing.weui.feature.hardware.screens

import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.showToast

@Composable
fun NFCScreen() {
    WeScreen(title = "NFC", description = "近场通信") {
        val context = LocalContext.current
        val nfcManager = context.getSystemService(Context.NFC_SERVICE) as? NfcManager
        val nfcAdapter = nfcManager?.defaultAdapter

        WeButton(text = "扫描NFC") {
            if (nfcAdapter == null) {
                context.showToast("此设备不支持NFC")
            } else if (nfcAdapter.isEnabled) {
                context.showToast("示例待完善")
            } else {
                context.showToast("NFC未开启")
                context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
        }
    }
}