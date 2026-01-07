package top.chengdongqing.weui.core.ui.components.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.utils.getFileProviderUri
import top.chengdongqing.weui.core.utils.rememberSingleThreadExecutor
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

@Stable
interface CameraState {
    /**
     * 相机预览视图
     */
    val previewView: PreviewView

    /**
     * 拍摄类型
     */
    val type: VisualMediaType

    /**
     * 是否开启闪光灯
     */
    val isFlashOn: Boolean

    /**
     * 是否正在录制视频
     */
    val isRecording: Boolean

    /**
     * 是否正在使用前置摄像头
     */
    val isUsingFrontCamera: Boolean

    /**
     * 当前视频录制进度
     */
    val videoProgress: Float

    /**
     * 当前对焦位置
     */
    val focusPoint: Offset?

    /**
     * 支持的变焦档位
     */
    val availableZoomSteps: List<Float>

    /**
     * 当前的变焦档位
     */
    val currentZoom: Float

    /**
     * 更新相机
     */
    fun updateCamera()

    /**
     * 拍照
     */
    fun takePhoto(onError: ((ImageCaptureException) -> Unit)? = null)

    /**
     * 开始录视频
     */
    fun startRecording(onError: ((Throwable?) -> Unit)? = null)

    /**
     * 结束录视频
     */
    fun stopRecording()

    /**
     * 切换闪光灯状态
     */
    fun toggleFlash()

    /**
     * 切换摄像头
     */
    fun switchCamera()

    /**
     * 对焦
     */
    fun focus(offset: Offset)

    /**
     * 设置变焦倍数
     */
    fun setZoom(step: Float)

    /**
     * 释放资源
     */
    fun release()
}

@Composable
fun rememberCameraState(
    type: VisualMediaType,
    maxVideoDuration: Long = 15,
    onCapture: (Uri, VisualMediaType) -> Unit
): CameraState {
    val context = LocalContext.current.applicationContext
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = rememberSingleThreadExecutor()
    val coroutineScope = rememberCoroutineScope()

    val state = remember {
        CameraStateImpl(
            context,
            lifecycleOwner,
            executor,
            type,
            maxVideoDuration,
            coroutineScope,
            onCapture
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            state.release()
            executor.shutdown()
        }
    }

    return state
}

private class CameraStateImpl(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val executor: ExecutorService,
    override val type: VisualMediaType,
    private val maxVideoDuration: Long,
    private val coroutineScope: CoroutineScope,
    private val onCapture: (Uri, VisualMediaType) -> Unit
) : CameraState {
    override val previewView = PreviewView(context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        keepScreenOn = true
    }
    override var isFlashOn by mutableStateOf(false)
        private set
    override var isRecording by mutableStateOf(false)
        private set
    override var isUsingFrontCamera by mutableStateOf(false)
        private set
    override val videoProgress
        get() = progressAnimate.value
    override var focusPoint by mutableStateOf<Offset?>(null)
        private set
    override var availableZoomSteps by mutableStateOf(listOf(1f))
        private set
    override var currentZoom by mutableFloatStateOf(1f)
        private set

    override fun updateCamera() {
        // 构建预览用例
        val preview = Preview.Builder()
            .setResolutionSelector(resolutionSelector)
            .build()
            .apply {
                surfaceProvider = previewView.surfaceProvider
            }
        val cameraSelector = if (isUsingFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                videoCapture
            )

            camera?.cameraInfo?.zoomState?.observe(lifecycleOwner) { state ->
                currentZoom = state.zoomRatio
                minZoom = state.minZoomRatio
                maxZoom = state.maxZoomRatio

                // 动态生成档位
                availableZoomSteps = calculatePhysicalSteps(state.minZoomRatio, state.maxZoomRatio)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculatePhysicalSteps(min: Float, max: Float): List<Float> {
        val steps = mutableListOf<Float>()

        // 如果支持超广角 (min < 1.0)
        if (min < 1f) steps.add(min)
        // 默认 1.0
        steps.add(1f)
        // 动态探测长焦
        val potentialTelephotos = listOf(2f, 5f, 10f)
        potentialTelephotos.forEach {
            if (it <= max && it > 1f) {
                steps.add(it)
            }
        }

        return steps.distinct().sorted()
    }

    override fun takePhoto(onError: ((ImageCaptureException) -> Unit)?) {
        val tempFile = File.createTempFile("IMG_", ".jpg")

        val metadata = ImageCapture.Metadata()
        // 自拍时镜像处理
        metadata.isReversedHorizontal = isUsingFrontCamera

        val outputOptions = ImageCapture.OutputFileOptions.Builder(tempFile)
            .setMetadata(metadata)
            .build()

        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = context.getFileProviderUri(tempFile)
                    onCapture(uri, VisualMediaType.IMAGE)
                }

                override fun onError(e: ImageCaptureException) {
                    e.printStackTrace()
                    onError?.invoke(e)
                }
            }
        )
    }

    @SuppressLint("MissingPermission")
    override fun startRecording(onError: ((Throwable?) -> Unit)?) {
        isRecording = true

        val milliseconds = maxVideoDuration.seconds.inWholeMilliseconds
        val tempFile = File.createTempFile("VID_", ".mp4")
        val outputOptions = FileOutputOptions.Builder(tempFile)
            .setDurationLimitMillis(milliseconds)
            .build()

        recordingInstance = videoCapture.output
            .prepareRecording(context, outputOptions)
            .withAudioEnabled()
            .start(executor) { event ->
                when (event) {
                    is VideoRecordEvent.Start -> {
                        coroutineScope.launch {
                            progressAnimate.snapTo(0f)
                            progressAnimate.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = milliseconds.toInt(),
                                    easing = LinearEasing
                                )
                            )

                            stopRecording()
                        }
                    }

                    is VideoRecordEvent.Finalize -> {
                        // 无论成功失败，显式停止动画
                        coroutineScope.launch {
                            if (progressAnimate.isRunning) {
                                progressAnimate.stop()
                            }
                        }

                        if (event.hasError()) {
                            event.cause?.printStackTrace()
                            onError?.invoke(event.cause)
                        } else {
                            val uri = context.getFileProviderUri(tempFile)
                            onCapture(uri, VisualMediaType.VIDEO)
                        }
                    }
                }
            }
    }

    override fun stopRecording() {
        isRecording = false

        recordingInstance?.stop()
        recordingInstance = null

        if (progressAnimate.isRunning) {
            coroutineScope.launch {
                progressAnimate.stop()
            }
        }
    }

    override fun toggleFlash() {
        isFlashOn = !isFlashOn
        camera?.let {
            if (it.cameraInfo.hasFlashUnit()) {
                it.cameraControl.enableTorch(isFlashOn)
            }
        }
    }

    override fun switchCamera() {
        isUsingFrontCamera = !isUsingFrontCamera
        isFlashOn = false
        currentZoom = 1f
        updateCamera()
    }

    override fun focus(offset: Offset) {
        val factory = previewView.meteringPointFactory
        // 将屏幕坐标转换为相机坐标点
        val point = factory.createPoint(offset.x, offset.y)

        // 构建对焦动作
        val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
            // 5秒后自动取消对焦锁定
            .setAutoCancelDuration(5, TimeUnit.SECONDS)
            .build()

        // 执行物理对焦
        camera?.cameraControl?.startFocusAndMetering(action)

        // 更新 UI 状态并自动隐藏
        focusPoint = offset
        focusJob?.cancel()
        focusJob = coroutineScope.launch {
            delay(2000) // 2秒后对焦框消失
            focusPoint = null
        }
    }

    override fun setZoom(step: Float) {
        currentZoom = step

        // 安全检查
        val targetRatio = step.coerceIn(minZoom, maxZoom)
        // 执行变焦
        camera?.cameraControl?.setZoomRatio(targetRatio)
    }

    override fun release() {
        try {
            recordingInstance?.stop()
            recordingInstance = null
            cameraProvider.unbindAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val cameraProvider: ProcessCameraProvider by lazy {
        ProcessCameraProvider.getInstance(context).get()
    }
    private val resolutionSelector = ResolutionSelector.Builder()
        .setAspectRatioStrategy(
            // 定义比例策略：首选 16:9，如果不支持，则找最接近的
            AspectRatioStrategy(
                AspectRatio.RATIO_16_9,
                AspectRatioStrategy.FALLBACK_RULE_AUTO
            )
        )
        .build()
    private var camera by mutableStateOf<Camera?>(null)
    private val imageCapture: ImageCapture by lazy {
        ImageCapture.Builder()
            .setResolutionSelector(resolutionSelector)
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }
    private val videoCapture: VideoCapture<Recorder> by lazy {
        VideoCapture.withOutput(Recorder.Builder().build())
    }
    private val progressAnimate by lazy { Animatable(0f) }
    private var recordingInstance: Recording? = null
    private var focusJob: Job? = null
    private var minZoom: Float = 0f
    private var maxZoom: Float = 0f
}
