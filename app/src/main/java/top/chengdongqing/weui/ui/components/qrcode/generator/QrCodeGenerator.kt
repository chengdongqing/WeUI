package top.chengdongqing.weui.ui.components.qrcode.generator

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun WeQrCodeGenerator(content: String, size: Int, color: Int = Color.BLACK) {
    val bitmap = produceState<ImageBitmap?>(initialValue = null, key1 = content, key2 = size) {
        value = withContext(Dispatchers.IO) {
            generateQrCode(content, size, color).asImageBitmap()
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
            Color.TRANSPARENT
        }
    }
    return Bitmap.createBitmap(pixels, size, size, Bitmap.Config.ARGB_8888)
}