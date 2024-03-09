package top.chengdongqing.weui.ui.screens.demo.imagecropper

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import top.chengdongqing.weui.ui.components.button.ButtonType
import top.chengdongqing.weui.ui.components.button.WeButton
import top.chengdongqing.weui.ui.components.imagecropper.WeImageCropper
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.ui.theme.WeUITheme

@Composable
fun ImageCropperScreen() {
    WeScreen(title = "ImageCropper", description = "图片裁剪", scrollEnabled = false) {
        var uri by remember { mutableStateOf<Uri?>(null) }
        var croppedImage by remember { mutableStateOf<Bitmap?>(null) }

        val pickMediaLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) {
            uri = it
        }

        uri?.let {
            if (croppedImage == null) {
                WeImageCropper(it) { bitmap ->
                    croppedImage = bitmap
                }
            } else {
                AsyncImage(
                    model = croppedImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "清除图片", type = ButtonType.PLAIN) {

            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        WeButton(text = "选择图片") {
            pickMediaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewImageCropper() {
    WeUITheme {
        ImageCropperScreen()
    }
}