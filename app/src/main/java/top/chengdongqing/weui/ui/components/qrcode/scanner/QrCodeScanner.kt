package top.chengdongqing.weui.ui.components.qrcode.scanner

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
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
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.WeToastOptions
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import java.util.concurrent.Executors

@Composable
fun WeQrCodeScanner(onChange: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var camera by remember { mutableStateOf<Camera?>(null) }
    val scanningExecutor = remember { Executors.newSingleThreadExecutor() }
    val toast = rememberWeToast()

    DisposableEffect(Unit) {
        onDispose {
            scanningExecutor.shutdown()
        }
    }

    Box {
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
                val barcodeAnalyzer = RealtimeBarcodeAnalyzer { barcodes ->
                    barcodes.firstOrNull()?.rawValue?.let(onChange)
                }
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build().apply {
                        setAnalyzer(scanningExecutor, barcodeAnalyzer)
                    }

                try {
                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        }
        ScannerDecoration()
        ScannerTools(camera) { uri ->
            scanFromPhoto(context, uri) { barcodes ->
                barcodes.firstOrNull()?.rawValue?.let(onChange)
                    ?: toast.show(WeToastOptions("识别失败", ToastIcon.FAIL))
            }
        }
    }
}

private fun scanFromPhoto(context: Context, uri: Uri, onChange: (List<Barcode>) -> Unit) {
    val image = InputImage.fromFilePath(context, uri)
    val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
    )

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            onChange(barcodes)
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
        }
}

private class RealtimeBarcodeAnalyzer(
    private val onChange: (List<Barcode>) -> Unit
) : ImageAnalysis.Analyzer {
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
        )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                onChange(barcodes)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}