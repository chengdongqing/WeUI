package top.chengdongqing.weui.ui.views.qrcode.scanner

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.rememberToggle

@Composable
internal fun BoxScope.ScannerTools(camera: Camera?, onPhotoSelected: (Uri) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(40.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val (isFlashlightOn, toggleFlashlight) = rememberToggle(
            defaultValue = false,
            reverseValue = true
        )
        ToolItem(
            label = "闪光灯",
            icon = if (isFlashlightOn) Icons.Filled.FlashlightOn else Icons.Filled.FlashlightOff,
            iconColor = if (isFlashlightOn) PrimaryColor else Color.White
        ) {
            camera?.let {
                if (camera.cameraInfo.hasFlashUnit()) {
                    camera.cameraControl.enableTorch(toggleFlashlight())
                }
            }
        }

        val pickMediaLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) {
            it?.let { uri ->
                onPhotoSelected(uri)
            }
        }
        ToolItem(label = "相册", icon = Icons.Filled.Image) {
            pickMediaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }
}

@Composable
private fun ToolItem(
    label: String,
    icon: ImageVector,
    iconColor: Color = Color.White,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f))
                .clickable { onClick() }
                .padding(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.White, fontSize = 13.sp)
    }
}