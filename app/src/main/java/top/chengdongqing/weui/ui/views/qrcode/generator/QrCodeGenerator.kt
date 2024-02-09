package top.chengdongqing.weui.ui.views.qrcode.generator

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.basic.WePage
import top.chengdongqing.weui.ui.components.feedback.ToastIcon
import top.chengdongqing.weui.ui.components.feedback.WeToastOptions
import top.chengdongqing.weui.ui.components.feedback.rememberWeToast
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton
import top.chengdongqing.weui.ui.components.form.WeTextarea
import kotlin.math.roundToInt

@Composable
fun QrCodeGeneratePage() {
    val size = rememberScreenWidth()
    var content by remember { mutableStateOf("https://weui.io") }
    var finalContent by remember { mutableStateOf("") }
    val toast = rememberWeToast()

    WePage(
        title = "QrCodeGenerator",
        description = "二维码生成",
        backgroundBrush = Brush.verticalGradient(listOf(Color(0xFF7A99CB), Color(0xFF8EA0BC)))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            WeTextarea(value = content, placeholder = "请输入内容", topBorder = true) {
                content = it
            }
            Spacer(modifier = Modifier.height(20.dp))
            WeButton(text = "生成二维码", type = ButtonType.PLAIN) {
                if (content.isNotEmpty()) {
                    finalContent = content
                } else {
                    toast.show(WeToastOptions("请输入内容", ToastIcon.FAIL))
                }
            }
            Spacer(modifier = Modifier.height(60.dp))
            if (finalContent.isNotEmpty()) {
                QrCodeImage(content = finalContent, size)
            }
        }
    }
}

@Composable
private fun rememberScreenWidth(fraction: Float = 0.6f): Int {
    val density = LocalDensity.current
    val configuration = LocalConfiguration.current

    return remember {
        with(density) {
            (configuration.screenWidthDp * fraction).dp.toPx().roundToInt()
        }
    }
}

@Composable
private fun QrCodeImage(content: String, size: Int) {
    val bitmap = produceState<ImageBitmap?>(initialValue = null, key1 = content, key2 = size) {
        value = withContext(Dispatchers.IO) {
            generateQrCode(content, size, size).asImageBitmap()
        }
    }

    bitmap.value?.let {
        Image(bitmap = it, contentDescription = "二维码")
    }
}

private fun generateQrCode(content: String, width: Int, height: Int): Bitmap {
    val hints = mapOf(EncodeHintType.MARGIN to 0)
    val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints)
    val pixels = IntArray(width * height) { pos ->
        if (bitMatrix.get(pos % width, pos / width)) {
            android.graphics.Color.WHITE
        } else {
            android.graphics.Color.TRANSPARENT
        }
    }
    return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
}