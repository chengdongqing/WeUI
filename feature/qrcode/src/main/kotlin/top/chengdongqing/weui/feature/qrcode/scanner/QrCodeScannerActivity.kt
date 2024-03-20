package top.chengdongqing.weui.feature.qrcode.scanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import top.chengdongqing.weui.core.ui.theme.WeUITheme

class QrCodeScannerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeUITheme {
                WeQrCodeScanner(
                    onRevoked = { finish() }
                ) { codes ->
                    val intent = Intent().apply {
                        putExtra("codes", codes.map { it.rawValue }.toTypedArray())
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, QrCodeScannerActivity::class.java)
    }
}