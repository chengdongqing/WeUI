package top.chengdongqing.weui.feature.qrcode.utils

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.feature.qrcode.scanner.QrCodeScannerActivity

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