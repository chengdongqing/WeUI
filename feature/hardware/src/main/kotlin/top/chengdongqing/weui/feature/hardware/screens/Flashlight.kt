package top.chengdongqing.weui.feature.hardware.screens

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.ui.components.switch.WeSwitch
import top.chengdongqing.weui.core.utils.isTrue

@Composable
fun FlashlightScreen() {
    WeScreen(title = "Flashlight", description = "闪光灯") {
        val context = LocalContext.current
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = rememberFlashAvailableCameraId(cameraManager)
        var isFlashOn by remember { mutableStateOf(false) }

        DisposableEffect(isFlashOn) {
            cameraId?.let {
                cameraManager.setTorchMode(cameraId, isFlashOn)
            }

            onDispose {
                if (isFlashOn && cameraId != null) {
                    cameraManager.setTorchMode(cameraId, false)
                }
            }
        }

        WeSwitch(isFlashOn) { checked ->
            cameraId?.let {
                isFlashOn = checked
            } ?: Toast.makeText(context, "此设备不支持闪光灯", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
private fun rememberFlashAvailableCameraId(cameraManager: CameraManager): String? {
    return remember {
        cameraManager.cameraIdList.find {
            cameraManager.getCameraCharacteristics(it)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE).isTrue()
        }
    }
}