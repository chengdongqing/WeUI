package top.chengdongqing.weui.feature.media.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Rational
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import top.chengdongqing.weui.core.data.model.VisualMediaType
import top.chengdongqing.weui.core.utils.rememberSingleThreadExecutor
import java.io.File
import java.util.concurrent.ExecutorService
import kotlin.time.Duration.Companion.seconds

@Stable
interface CameraState {
    /**
     * 相机预览视图
     */
    val previewView: PreviewView

    /**
     * 用于管理camera生命周期等
     */
    val cameraProvider: ProcessCameraProvider

    /**
     * 用于捕获图片
     */
    val imageCapture: ImageCapture

    /**
     * 用于捕获视频
     */
    val videoCapture: VideoCapture<Recorder>

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
     * 视频最大录制时长
     */
    val maxVideoDuration: Long

    /**
     * 当前视频录制时长
     */
    val videoProgress: Float

    /**
     * 拍照
     */
    fun takePhoto(onError: ((ImageCaptureException) -> Unit)? = null, onSuccess: (Uri) -> Unit)

    /**
     * 开始录视频
     */
    fun startRecording(onError: ((Throwable?) -> Unit)? = null, onSuccess: (Uri) -> Unit)

    /**
     * 结束录视频
     */
    fun stopRecording()

    /**
     * 切换闪光灯状态
     */
    fun toggleFlashState()

    /**
     * 切换摄像头
     */
    fun toggleCameraSelector()
}

@Composable
fun rememberCameraState(type: VisualMediaType, maxVideoDuration: Long = 15): CameraState {
    val context = LocalContext.current
    val videoView = remember {
        PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            keepScreenOn = true
        }
    }
    val executor = rememberSingleThreadExecutor()
    val coroutineScope = rememberCoroutineScope()

    return remember {
        CameraStateImpl(context, videoView, type, maxVideoDuration, executor, coroutineScope)
    }
}

private class CameraStateImpl(
    private val context: Context,
    override val previewView: PreviewView,
    override val type: VisualMediaType,
    override val maxVideoDuration: Long,
    private val executor: ExecutorService,
    private val coroutineScope: CoroutineScope
) : CameraState {
    override val cameraProvider: ProcessCameraProvider =
        ProcessCameraProvider.getInstance(context).get()
    override val imageCapture: ImageCapture = ImageCapture.Builder().build()
    override val videoCapture: VideoCapture<Recorder> =
        VideoCapture.withOutput(Recorder.Builder().build())
    override var isFlashOn by mutableStateOf(false)
    override var isRecording by mutableStateOf(false)
    override var isUsingFrontCamera by mutableStateOf(false)
    override val videoProgress get() = progressAnimate.value

    override fun takePhoto(onError: ((ImageCaptureException) -> Unit)?, onSuccess: (Uri) -> Unit) {
        val tempFile = File.createTempFile("IMG_", ".jpg").apply {
            deleteOnExit()
        }
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(tempFile).build()

        imageCapture.takePicture(
            outputFileOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
                        .let(onSuccess)
                }

                override fun onError(e: ImageCaptureException) {
                    e.printStackTrace()
                    onError?.invoke(e)
                }
            }
        )
    }

    @SuppressLint("MissingPermission")
    override fun startRecording(onError: ((Throwable?) -> Unit)?, onSuccess: (Uri) -> Unit) {
        isRecording = true

        val milliseconds = maxVideoDuration.seconds.inWholeMilliseconds
        val tempFile = File.createTempFile("VID_", ".mp4").apply {
            deleteOnExit()
        }
        val outputFileOptions = FileOutputOptions.Builder(tempFile)
            .setDurationLimitMillis(milliseconds)
            .build()

        recordingInstance = videoCapture.output
            .prepareRecording(context, outputFileOptions)
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
                        if (event.hasError()) {
                            event.cause?.printStackTrace()
                            onError?.invoke(event.cause)
                        } else {
                            FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                tempFile
                            ).let(onSuccess)
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

    override fun toggleFlashState() {
        imageCapture.flashMode = if (isFlashOn) {
            ImageCapture.FLASH_MODE_OFF
        } else {
            ImageCapture.FLASH_MODE_ON
        }
        isFlashOn = !isFlashOn
    }

    override fun toggleCameraSelector() {
        isUsingFrontCamera = !isUsingFrontCamera
    }

    private val progressAnimate = Animatable(0f)
    private var recordingInstance: Recording? = null

    init {
        imageCapture.setCropAspectRatio(Rational(9, 16))
    }
}