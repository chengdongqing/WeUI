package top.chengdongqing.weui.ui.components.qrcode.scanner

import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.common.Barcode
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.rememberToastState
import top.chengdongqing.weui.utils.RequestCameraPermission
import java.util.concurrent.Executors

@Composable
fun WeQrCodeScanner(onRevoked: () -> Unit, onChange: (List<Barcode>) -> Unit) {
    val context = LocalContext.current
    var camera by remember { mutableStateOf<Camera?>(null) }
    val toast = rememberToastState()

    RequestCameraPermission(onRevoked = onRevoked) {
        CameraView(setCamera = { camera = it }, onChange)
        ScannerDecoration()
        ScannerTools(camera) { uri ->
            decodeBarcodeFromUri(context, uri) { barcodes ->
                if (barcodes.isNotEmpty()) {
                    onChange(barcodes)
                } else {
                    toast.show("识别失败", ToastIcon.FAIL)
                }
            }
        }
    }
}

@Composable
private fun CameraView(setCamera: (Camera) -> Unit, onChange: (List<Barcode>) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scanningExecutor = remember { Executors.newSingleThreadExecutor() }

    DisposableEffect(Unit) {
        onDispose {
            scanningExecutor.shutdown()
        }
    }

    AndroidView(
        factory = { PreviewView(context) },
        modifier = Modifier.fillMaxSize()
    ) { previewView ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val barcodeAnalyzer = BarcodeAnalyzer { barcodes ->
                if (barcodes.isNotEmpty()) {
                    onChange(barcodes)
                }
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().apply { setAnalyzer(scanningExecutor, barcodeAnalyzer) }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                ).also(setCamera)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }
}