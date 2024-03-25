package top.chengdongqing.weui.feature.qrcode.scanner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.barcode.common.Barcode
import top.chengdongqing.weui.core.utils.RequestCameraPermission

@Composable
fun WeQrCodeScanner(onRevoked: () -> Unit, onChange: (List<Barcode>) -> Unit) {
    val state = rememberScannerState(onChange)

    RequestCameraPermission(onRevoked = onRevoked) {
        CameraView(state)
        ScannerDecoration()
        ScannerTools(state)
    }
}

@Composable
private fun CameraView(state: ScannerState) {
    AndroidView(
        factory = { state.previewView },
        modifier = Modifier.fillMaxSize(),
        update = { state.updateCamera() }
    )
}