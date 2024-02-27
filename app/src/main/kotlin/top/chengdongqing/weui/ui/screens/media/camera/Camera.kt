package top.chengdongqing.weui.ui.screens.media.camera

import android.content.Context
import android.util.Rational
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import top.chengdongqing.weui.ui.components.toast.ToastIcon
import top.chengdongqing.weui.ui.components.toast.ToastOptions
import top.chengdongqing.weui.ui.components.toast.ToastState
import top.chengdongqing.weui.ui.components.toast.rememberWeToast
import top.chengdongqing.weui.utils.MediaStoreUtils
import top.chengdongqing.weui.utils.MediaType
import top.chengdongqing.weui.utils.RequestCameraPermission
import top.chengdongqing.weui.utils.rememberToggleState

@Composable
fun CameraScreen(navController: NavController) {
    RequestCameraPermission(navController) {
        Camera()
    }
}

@Composable
private fun Camera() {
    val context = LocalContext.current
    val view = LocalView.current
    val imageCapture = remember {
        ImageCapture.Builder()
            // 照片的方向设置为手机当前的方向
            .setTargetRotation(view.display.rotation)
            .build()
    }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val toast = rememberWeToast()
    val (flashMode, toggleFlashMode) = rememberToggleState(
        ImageCapture.FLASH_MODE_OFF,
        ImageCapture.FLASH_MODE_ON
    )
    val (_, toggleCameraSelector) = rememberToggleState(
        CameraSelector.DEFAULT_BACK_CAMERA,
        CameraSelector.DEFAULT_FRONT_CAMERA
    )

    Column {
        CameraPreview(imageCapture, cameraSelector)
        ControlBar(
            isFlashOn = flashMode == ImageCapture.FLASH_MODE_ON,
            onTakePhoto = { takePhoto(context, imageCapture, toast) },
            onToggleFlash = {
                imageCapture.flashMode = toggleFlashMode()
            },
            onSwitchCamera = {
                cameraSelector = toggleCameraSelector()
            }
        )
    }
}

@Composable
private fun ColumnScope.CameraPreview(
    imageCapture: ImageCapture,
    cameraSelector: CameraSelector
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                keepScreenOn = true
            }
            cameraProvider = ProcessCameraProvider.getInstance(context).get()
            imageCapture.setCropAspectRatio(Rational(9, 16))
            previewView
        },
        modifier = Modifier.weight(1f)
    ) { previewView ->
        try {
            val preview = Preview.Builder().build().also { preview ->
                preview.setSurfaceProvider(previewView.surfaceProvider)
            }
            cameraProvider?.unbindAll()
            cameraProvider?.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }
}

@Composable
private fun ControlBar(
    isFlashOn: Boolean,
    onTakePhoto: () -> Unit,
    onToggleFlash: () -> Unit,
    onSwitchCamera: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(vertical = 30.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onToggleFlash) {
            Icon(
                if (isFlashOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                contentDescription = "开关闪光灯",
                tint = Color.White
            )
        }
        Box(
            modifier = Modifier
                .size(80.dp)
                .clickable(onClick = onTakePhoto)
                .border(2.dp, Color.White, CircleShape)
                .padding(8.dp)
                .background(Color.White, CircleShape)
        )
        IconButton(onClick = onSwitchCamera) {
            Icon(
                Icons.Filled.FlipCameraAndroid,
                contentDescription = "切换相机",
                tint = Color.White
            )
        }
    }
}

private fun takePhoto(context: Context, imageCapture: ImageCapture, toast: ToastState) {
    val contentUri = MediaStoreUtils.getContentUri(MediaType.IMAGE)
    val contentValues = MediaStoreUtils.createContentValues(
        filename = "IMG_${System.currentTimeMillis()}.jpg",
        mimeType = "image/jpeg",
        mediaType = MediaType.IMAGE,
        context
    )
    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
        context.contentResolver,
        contentUri,
        contentValues
    ).build()

    imageCapture.takePicture(
        outputFileOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                toast.show(ToastOptions("照片已保存", ToastIcon.SUCCESS))
            }

            override fun onError(e: ImageCaptureException) {
                e.printStackTrace()
                toast.show(ToastOptions("拍照失败", ToastIcon.FAIL))
            }
        }
    )
}
