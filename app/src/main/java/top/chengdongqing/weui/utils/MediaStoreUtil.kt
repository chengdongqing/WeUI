package top.chengdongqing.weui.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.R
import java.io.File
import java.io.IOException

object MediaStoreUtil {

    /**
     * 保存媒体文件到相册
     *
     * @param context 上下文
     * @param mediaUri 文件URI
     * @param filename 文件名
     * @param mimeType MIME类型
     * @param mediaType 媒体类型
     * @return 是否保存成功
     */
    suspend fun saveMediaToGallery(
        context: Context,
        mediaUri: Uri,
        filename: String,
        mimeType: String,
        mediaType: MediaType
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            // 应用名称作为默认的自定义文件夹名称
            val appName = context.getString(R.string.app_name)
            val contentValues = createContentValues(filename, mimeType, mediaType, appName)
            val contentUri = getContentUri(mediaType)

            context.contentResolver.insert(contentUri, contentValues)?.let { uri ->
                // 将文件写入到指定的Uri
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    File(mediaUri.path!!).inputStream().use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                // 更新文件的状态，表示写入操作完成
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val contentValues1 = ContentValues().apply {
                        put(MediaStore.MediaColumns.IS_PENDING, 0)
                    }
                    context.contentResolver.update(uri, contentValues1, null, null)
                }
                true
            } ?: false
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun createContentValues(
        filename: String,
        mimeType: String,
        mediaType: MediaType,
        appName: String
    ): ContentValues =
        ContentValues().apply {
            // 设置文件名
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            // 设置MIME类型
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val directory = when (mediaType) {
                    MediaType.IMAGE -> Environment.DIRECTORY_PICTURES
                    MediaType.VIDEO -> Environment.DIRECTORY_MOVIES
                }
                val relativePath = "$directory/$appName"
                // 设置文件的相对路径
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
                // 标记文件为等待状态
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

    fun getContentUri(mediaType: MediaType): Uri =
        when (mediaType) {
            MediaType.IMAGE -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            MediaType.VIDEO -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
        }
}

enum class MediaType {
    IMAGE, VIDEO
}