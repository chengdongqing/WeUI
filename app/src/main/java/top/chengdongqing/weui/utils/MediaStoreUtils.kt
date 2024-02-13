package top.chengdongqing.weui.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import top.chengdongqing.weui.R

enum class MediaType {
    IMAGE, VIDEO, AUDIO
}

object MediaStoreUtils {
    fun createContentValues(
        filename: String,
        mimeType: String,
        mediaType: MediaType,
        context: Context
    ): ContentValues =
        ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            val directory = when (mediaType) {
                MediaType.IMAGE -> Environment.DIRECTORY_PICTURES
                MediaType.VIDEO -> Environment.DIRECTORY_MOVIES
                MediaType.AUDIO -> Environment.DIRECTORY_MUSIC
            }
            val appName = context.getString(R.string.app_name)
            val relativePath = "$directory/$appName"
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }

    fun getContentUri(mediaType: MediaType): Uri =
        when (mediaType) {
            MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaType.AUDIO -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
}