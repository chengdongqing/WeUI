package top.chengdongqing.weui.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import top.chengdongqing.weui.androidAppInstance
import top.chengdongqing.weui.core.data.model.MediaType
import top.chengdongqing.weui.shared.R
import top.chengdongqing.weui.util.MediaStoreUtils.createContentValues
import top.chengdongqing.weui.util.MediaStoreUtils.finishPending

actual suspend fun saveBitmap(
    bitmap: ImageBitmap,
    filename: String
): Boolean {
    return runCatching {
        androidAppInstance.applicationContext.apply {
            val contentValues = createContentValues(
                filename = filename,
                mimeType = "image/webp",
                mediaType = MediaType.IMAGE
            )

            val contentUri = MediaStoreUtils.getContentUri(MediaType.IMAGE)

            contentResolver.insert(contentUri, contentValues)?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap.asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 90, outputStream)
                    finishPending(uri)
                }
            }
        }
        true
    }.onFailure {
        it.printStackTrace()
    }.getOrDefault(false)
}

object MediaStoreUtils {
    /**
     * 创建 MediaStore 插入所需的 ContentValues
     * 核心逻辑：设置文件名、路径并开启 [android.provider.MediaStore.MediaColumns.IS_PENDING] 状态
     */
    fun Context.createContentValues(
        filename: String,
        mimeType: String,
        mediaType: MediaType,
    ): ContentValues {
        val directory = when (mediaType) {
            MediaType.IMAGE -> Environment.DIRECTORY_PICTURES
            MediaType.VIDEO -> Environment.DIRECTORY_MOVIES
            MediaType.AUDIO -> Environment.DIRECTORY_MUSIC
            MediaType.RECORDING -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Environment.DIRECTORY_RECORDINGS
            } else {
                Environment.DIRECTORY_MUSIC
            }
        }
        val appName = getString(R.string.app_name)
        val relativePath = "$directory/$appName"

        return createContentValues(filename, mimeType, relativePath)
    }

    fun createContentValues(
        filename: String,
        mimeType: String,
        relativePath: String,
    ): ContentValues =
        ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

    /**
     * 文件写入完成后，取消挂起状态，使媒体文件在相册中可见
     */
    fun Context.finishPending(uri: Uri) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        contentResolver.update(uri, contentValues, null, null)
    }

    /**
     * 根据媒体类型获取对应的 MediaStore 系统表 Uri
     */
    fun getContentUri(mediaType: MediaType): Uri =
        when (mediaType) {
            MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaType.AUDIO, MediaType.RECORDING -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
}