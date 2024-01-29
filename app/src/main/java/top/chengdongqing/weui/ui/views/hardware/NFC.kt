package top.chengdongqing.weui.ui.views.hardware

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.NfcManager
import android.nfc.tech.IsoDep
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.utils.bytesToHex
import top.chengdongqing.weui.utils.hexToBytes

@Composable
fun NFCPage() {
    WePage(title = "NFC", description = "近场通信") {
        val context = LocalContext.current
        val nfcAdapter =
            (context.getSystemService(Context.NFC_SERVICE) as NfcManager).defaultAdapter

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeButton(text = "扫描NFC") {
                if (nfcAdapter.isEnabled) {
                    nfcAdapter.enableReaderMode(context as Activity, {
                        val isoDep = IsoDep.get(it)
                        if (!isoDep.isConnected) {
                            isoDep.connect()
                        }
                        var result: String =
                            bytesToHex(isoDep.transceive(hexToBytes("00A404000B485A2E4A4D2E4144463031")))
                        Log.e("readIsoCard", result)
                        result = bytesToHex(isoDep.transceive(hexToBytes("00B0950030")))
                        Log.e("readIsoCard", result)
                        isoDep.close()
                    }, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
                } else {
                    Toast.makeText(context, "NFC未开启", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
                }
            }
        }
    }
}