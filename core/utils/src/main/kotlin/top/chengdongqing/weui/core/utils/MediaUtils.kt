package top.chengdongqing.weui.core.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import java.io.File
import java.io.IOException

object MediaStoreUtils {
    fun createContentValues(
        filename: String,
        mediaType: MediaType,
        mimeType: String,
        context: Context
    ): ContentValues =
        ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
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
            val appName = context.getString(R.string.app_name)
            val relativePath = "$directory/$appName"
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

    fun finishPending(uri: Uri, context: Context) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        context.contentResolver.update(uri, contentValues, null, null)
    }

    fun getContentUri(mediaType: MediaType): Uri =
        when (mediaType) {
            MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaType.AUDIO, MediaType.RECORDING -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
}

fun Context.shareFile(file: File, mimeType: String = "image/*") {
    val sharingUri = this.getFileProviderUri(file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        this.type = mimeType
        putExtra(Intent.EXTRA_STREAM, sharingUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(intent)
}

suspend fun Context.loadMediaThumbnail(media: MediaItem): Any? {
    // 图片在低版本系统中加载原图
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !media.isVideo()) {
        return media.uri
    } else {
        return withContext(Dispatchers.IO) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // 高版本系统直接加载缩略图
                    contentResolver.loadThumbnail(
                        media.uri, Size(200, 200), null
                    )
                } else {
                    // 低版本系统获取视频首帧
                    loadVideoThumbnail(media.uri)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                if (media.isImage()) {
                    media.uri
                } else {
                    null
                }
            }
        }
    }
}

fun Context.loadVideoThumbnail(uri: Uri): Bitmap? {
    return MediaMetadataRetriever().use {
        it.setDataSource(this, uri)
        it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            ?.toInt()
        it.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            ?.toInt()
        it.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
    }
}