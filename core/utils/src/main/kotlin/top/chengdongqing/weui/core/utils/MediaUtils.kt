package top.chengdongqing.weui.core.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.MediaType
import top.chengdongqing.weui.core.data.model.isImage
import top.chengdongqing.weui.core.data.model.isVideo
import top.chengdongqing.weui.core.ui.theme.R
import java.io.IOException

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

/**
 * 加载媒体缩略图（兼容图片与视频）
 * @return 可能是 Uri (低版本图片), Bitmap (高版本或视频) 或 null
 */
suspend fun Context.loadMediaThumbnail(
    media: MediaItem,
    size: Size = Size(200, 200)
): Any? {
    // Android 10 以下的图片，直接返回原图 Uri
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !media.isVideo()) {
        return media.uri
    }

    return withContext(Dispatchers.IO) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // API 29+ 官方推荐的缩略图加载方式，系统会自动处理缓存
                contentResolver.loadThumbnail(
                    media.uri, size, null
                )
            } else {
                // API 29 以下视频文件需要手动提取首帧
                loadVideoThumbnail(media.uri)
            }
        } catch (_: IOException) {
            // 如果加载失败，降级处理：图片返回原图 Uri，视频返回 null
            if (media.isImage()) media.uri else null
        }
    }
}

/**
 * 针对低版本系统的视频首帧提取
 */
fun Context.loadVideoThumbnail(uri: Uri): Bitmap? {
    return MediaMetadataRetriever().use { retriever ->
        try {
            retriever.setDataSource(this, uri)
            // 提取第一秒或第一帧（关键帧），OPTION_CLOSEST_SYNC 性能较平衡
            retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (_: Exception) {
            null
        }
    }
}

/**
 * 跨 URI 的数据流拷贝（用于文件保存或导出）
 */
fun ContentResolver.copyUri(from: Uri, to: Uri): Boolean {
    return try {
        openInputStream(from)?.use { input ->
            openOutputStream(to)?.use { output ->
                input.copyTo(output)
                true
            }
        } ?: false
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}