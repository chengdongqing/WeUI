package top.chengdongqing.weui.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import top.chengdongqing.weui.R
import top.chengdongqing.weui.enums.MediaType

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

    fun finishPending(mediaUri: Uri, context: Context) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        context.contentResolver.update(mediaUri, contentValues, null, null)
    }

    fun getContentUri(mediaType: MediaType): Uri =
        when (mediaType) {
            MediaType.IMAGE -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            MediaType.VIDEO -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            MediaType.AUDIO, MediaType.RECORDING -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
}

fun Context.shareFile(uri: Uri, type: String = "image/*") {
    val sharingUri = FileProvider.getUriForFile(
        this,
        "${packageName}.provider",
        uri.toFile()
    )
    val intent = Intent(Intent.ACTION_SEND).apply {
        this.type = type
        putExtra(Intent.EXTRA_STREAM, sharingUri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(intent)
}