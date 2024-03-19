package top.chengdongqing.weui.feature.demos.imagecropper.screens

import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import top.chengdongqing.weui.core.data.model.MediaType
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.rememberPickMediasLauncher
import top.chengdongqing.weui.feature.demos.imagecropper.cropper.WeImageCropper

@Composable
fun ImageCropperScreen() {
    WeScreen(title = "ImageCropper", description = "图片裁剪", scrollEnabled = false) {
        var uri by remember { mutableStateOf<Uri?>(null) }
        var croppedImage by remember { mutableStateOf<Bitmap?>(null) }
        val pickMedia = rememberPickMediasLauncher {
            uri = it.first().uri
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
            pickMedia(MediaType.IMAGE, 1)
        }
    }
}