package top.chengdongqing.weui.ui.views.media.camera

import android.Manifest
import android.content.Context
import android.util.Rational
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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.WeToastOptions
import top.chengdongqing.weui.ui.components.feedback.WeToastState
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.utils.MediaStoreUtil
import top.chengdongqing.weui.utils.MediaType
import top.chengdongqing.weui.utils.SetupFullscreen
import top.chengdongqing.weui.utils.rememberToggle

@Composable
fun CameraPage(navController: NavController) {
    RequestCameraPermission(navController) {
        Camera()
    }
}

@Composable
private fun Camera() {
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    val toast = rememberWeToast()
    val (flashMode, toggleFlashMode) = rememberToggle(
        ImageCapture.FLASH_MODE_OFF,
        ImageCapture.FLASH_MODE_ON
    )
    val (_, toggleCameraSelector) = rememberToggle(
        CameraSelector.DEFAULT_BACK_CAMERA,
        CameraSelector.DEFAULT_FRONT_CAMERA
    )

    Column {
        CameraPreview(cameraProviderFuture, imageCapture, cameraSelector)
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
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    imageCapture: ImageCapture,
    cameraSelector: CameraSelector
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }

    AndroidView(
        factory = { context ->
            val previewView = PreviewView(context)
            cameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            imageCapture.setCropAspectRatio(Rational(9, 16))
            previewView
        },
        modifier = Modifier.weight(1f)
    ) {
        try {
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestCameraPermission(
    navController: NavController,
    content: @Composable () -> Unit
) {
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA) { res ->
        if (!res) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(permissionState) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (permissionState.status.isGranted) {
            SetupFullscreen()
            content()
        }
    }
}

private fun takePhoto(context: Context, imageCapture: ImageCapture, toast: WeToastState) {
    val contentUri = MediaStoreUtil.getContentUri(MediaType.IMAGE)
    val contentValues = MediaStoreUtil.createContentValues(
        filename = "IMG_${System.currentTimeMillis()}.jpg",
        mimeType = "image/jpeg",
        mediaType = MediaType.IMAGE,
        appName = context.getString(R.string.app_name)
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
                toast.show(WeToastOptions("照片已保存", ToastIcon.SUCCESS))
            }

            override fun onError(e: ImageCaptureException) {
                toast.show(WeToastOptions("拍照失败", ToastIcon.FAIL))
                e.printStackTrace()
            }
        }
    )
}
