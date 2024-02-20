package top.chengdongqing.weui.ui.screens.hardware

import android.content.Context
import android.content.Intent
import android.nfc.NfcManager
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.screen.WeScreen

@Composable
fun NFCScreen() {
    WeScreen(title = "NFC", description = "近场通信") {
        val context = LocalContext.current
        val nfcManager = context.getSystemService(Context.NFC_SERVICE) as? NfcManager
        val nfcAdapter = nfcManager?.defaultAdapter

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeButton(text = "扫描NFC") {
                if (nfcAdapter == null) {
                    Toast.makeText(context, "此设备不支持NFC", Toast.LENGTH_SHORT).show()
                } else if (nfcAdapter.isEnabled) {
                    Toast.makeText(context, "示例待完善", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "NFC未开启", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                }
            }
        }
    }
}