package top.chengdongqing.weui.feature.qrcode.scanner

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun rememberScanCodeLauncher(onChange: (Array<String>) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getStringArrayExtra("codes")?.let(onChange)
        }
    }

    return {
        launcher.launch(QrCodeScannerActivity.newIntent(context))
    }
}