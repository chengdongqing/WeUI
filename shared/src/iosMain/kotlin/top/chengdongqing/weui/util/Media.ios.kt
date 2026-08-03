package top.chengdongqing.weui.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.Foundation.create
import platform.Photos.PHAccessLevelAddOnly
import platform.Photos.PHAssetCreationRequest
import platform.Photos.PHAssetResourceCreationOptions
import platform.Photos.PHAssetResourceTypePhoto
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual suspend fun saveBitmap(
    bitmap: ImageBitmap,
    filename: String
): Boolean {
    val bytes = Image.makeFromBitmap(bitmap.asSkiaBitmap())
        .encodeToData(EncodedImageFormat.PNG, quality = 100)
        ?.bytes
        ?: return false

    val authorizationStatus = suspendCancellableCoroutine { continuation ->
        PHPhotoLibrary.requestAuthorizationForAccessLevel(PHAccessLevelAddOnly) { status ->
            if (continuation.isActive) continuation.resume(status)
        }
    }
    if (
        authorizationStatus != PHAuthorizationStatusAuthorized &&
        authorizationStatus != PHAuthorizationStatusLimited
    ) {
        return false
    }

    val imageData = bytes.usePinned { pinned ->
        NSData.create(bytes = pinned.addressOf(0), length = bytes.size.toULong())
    }
    val pngFilename = filename.substringBeforeLast('.', filename) + ".png"

    return suspendCancellableCoroutine { continuation ->
        PHPhotoLibrary.sharedPhotoLibrary().performChanges(
            changeBlock = {
                val options = PHAssetResourceCreationOptions().apply {
                    originalFilename = pngFilename
                    uniformTypeIdentifier = "public.png"
                }
                PHAssetCreationRequest.creationRequestForAsset().addResourceWithType(
                    type = PHAssetResourceTypePhoto,
                    data = imageData,
                    options = options
                )
            },
            completionHandler = { success, error ->
                error?.let { println("Failed to save image to Photos: $it") }
                if (continuation.isActive) continuation.resume(success)
            }
        )
    }
}
