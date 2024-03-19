package top.chengdongqing.weui.feature.qrcode.generator

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.Color as AndroidColor

@Composable
fun WeQrCodeGenerator(content: String, size: Int, color: Color = Color.Black) {
    val bitmap = produceState<ImageBitmap?>(initialValue = null, key1 = content, key2 = size) {
        value = withContext(Dispatchers.IO) {
            generateQrCode(content, size, color.toArgb()).asImageBitmap()
        }
    }

    bitmap.value?.let {
        Image(bitmap = it, contentDescription = "二维码")
    }
}

private fun generateQrCode(content: String, size: Int, color: Int): Bitmap {
    val hints = mapOf(EncodeHintType.MARGIN to 0)
    val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
    val pixels = IntArray(size * size) { pos ->
        if (bitMatrix.get(pos % size, pos / size)) {
            color
        } else {
            AndroidColor.TRANSPARENT
        }
    }
    return Bitmap.createBitmap(pixels, size, size, Bitmap.Config.ARGB_8888)
}