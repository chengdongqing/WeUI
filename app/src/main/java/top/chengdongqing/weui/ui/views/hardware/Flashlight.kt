package top.chengdongqing.weui.ui.views.hardware

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.form.WeSwitch

@Composable
fun FlashlightPage() {
    WePage(title = "Flashlight", description = "闪光灯") {
        val context = LocalContext.current
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

        val isFlashOn = remember { mutableStateOf(false) }

        WeSwitch(isFlashOn.value) { checked ->
            isFlashOn.value = checked
            cameraManager.setTorchMode(cameraManager.cameraIdList[0], checked)
        }
    }
}