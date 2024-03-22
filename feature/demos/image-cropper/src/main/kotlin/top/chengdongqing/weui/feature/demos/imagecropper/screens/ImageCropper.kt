package top.chengdongqing.weui.feature.demos.imagecropper.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import top.chengdongqing.weui.core.data.model.MediaType
import top.chengdongqing.weui.core.ui.components.button.ButtonType
import top.chengdongqing.weui.core.ui.components.button.WeButton
import top.chengdongqing.weui.core.ui.components.mediapicker.rememberPickMediasLauncher
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.feature.demos.imagecropper.cropper.rememberImageCropperLauncher

@Composable
fun ImageCropperScreen() {
    WeScreen(title = "ImageCropper", description = "图片裁剪", scrollEnabled = false) {
        var croppedImage by remember { mutableStateOf<ImageBitmap?>(null) }

        val context = LocalContext.current
        val launchImageCropper = rememberImageCropperLauncher { uri ->
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()?.let { imageBitmap ->
                    croppedImage = imageBitmap
                }
            }
        }
        val pickMedia = rememberPickMediasLauncher {
            val uri = it.first().uri
            launchImageCropper(uri)
        }

        croppedImage?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "清除图片", type = ButtonType.DANGER) {
                croppedImage = null
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        WeButton(text = "选择图片") {
            pickMedia(MediaType.IMAGE, 1)
        }
    }
}