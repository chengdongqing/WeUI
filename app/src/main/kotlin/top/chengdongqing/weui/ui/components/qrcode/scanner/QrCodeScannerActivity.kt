package top.chengdongqing.weui.ui.components.qrcode.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class QrCodeScannerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeQrCodeScanner(
                onRevoked = { finish() }
            ) { codes ->
                val intent = Intent().apply {
                    putExtra("codes", codes.map { it.rawValue }.toTypedArray())
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, QrCodeScannerActivity::class.java)
    }
}