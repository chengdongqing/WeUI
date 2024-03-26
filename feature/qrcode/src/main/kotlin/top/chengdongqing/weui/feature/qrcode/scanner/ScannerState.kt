package top.chengdongqing.weui.feature.qrcode.scanner

import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.barcode.common.Barcode
import top.chengdongqing.weui.core.utils.rememberSingleThreadExecutor
import java.util.concurrent.ExecutorService

@Stable
interface ScannerState {
    /**
     * 相机预览视图
     */
    val previewView: PreviewView

    /**
     * 是否开启闪光灯
     */
    val isFlashOn: Boolean

    /**
     * 更新相机
     */
    fun updateCamera()

    /**
     * 切换闪光灯状态
     */
    fun toggleFlashState()

    /**
     * 扫描指定图片
     */
    fun scanPhoto(uri: Uri, onFail: () -> Unit)
}

@Composable
fun rememberScannerState(onChange: (List<Barcode>) -> Unit): ScannerState {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = rememberSingleThreadExecutor()

    return remember {
        ScannerStateImpl(context, lifecycleOwner, executor, onChange)
    }
}

private class ScannerStateImpl(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val executor: ExecutorService,
    private val onChange: (List<Barcode>) -> Unit
) : ScannerState {
    override val previewView by lazy {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            keepScreenOn = true
        }
    }
    override var isFlashOn by mutableStateOf(false)

    override fun updateCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraProvider = cameraProviderFuture.get()
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
            .build().apply { setAnalyzer(executor, barcodeAnalyzer) }

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
    }

    override fun toggleFlashState() {
        isFlashOn = !isFlashOn
        camera?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                it.cameraControl.enableTorch(isFlashOn)
            }
        }
    }

    override fun scanPhoto(uri: Uri, onFail: () -> Unit) {
        decodeBarcodeFromUri(context, uri) { barcodes ->
            if (barcodes.isNotEmpty()) {
                onChange(barcodes)
            } else {
                onFail()
            }
        }
    }

    private var camera: Camera? = null
}