package top.chengdongqing.weui.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.browser.document
import org.jetbrains.skia.Image
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

@OptIn(ExperimentalWasmJsInterop::class)
actual suspend fun saveBitmap(bitmap: ImageBitmap, filename: String): Boolean {
    return try {
        val image = Image.makeFromBitmap(bitmap.asSkiaBitmap())
        val bytes = image.encodeToData(quality = 90)?.bytes ?: return false

        val jsArray = Uint8Array(bytes.size)
        bytes.forEachIndexed { index, byte -> jsArray[index] = byte }
        val jsAnyArray = arrayOf(jsArray as JsAny?).toJsArray()

        val blob = Blob(jsAnyArray, BlobPropertyBag(type = "image/webp"))
        val url = URL.createObjectURL(blob)

        val a = document.createElement("a") as HTMLAnchorElement
        a.href = url
        a.download = filename
        document.body?.appendChild(a)
        a.click()

        document.body?.removeChild(a)
        URL.revokeObjectURL(url)

        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}