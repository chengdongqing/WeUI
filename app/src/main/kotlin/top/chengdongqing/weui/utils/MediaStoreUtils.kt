package top.chengdongqing.weui.utils

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import top.chengdongqing.weui.R
import top.chengdongqing.weui.ui.screens.demo.gallery.MediaItem
import top.chengdongqing.weui.ui.screens.demo.gallery.preview.MediaPreviewActivity

enum class MediaType {
    IMAGE, VIDEO, AUDIO, RECORDING
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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestMediaPermission(content: @Composable () -> Unit) {
    val permissionState = rememberMultiplePermissionsState(
        remember {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            } else {
                listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    )

    LaunchedEffect(permissionState) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (permissionState.allPermissionsGranted) {
        content()
    }
}

fun Context.previewMedias(items: List<MediaItem>, current: Int = 0) {
    val intent = MediaPreviewActivity.newIntent(this).apply {
        putExtra("uris", items.map { it.path }.toTypedArray())
        putExtra("current", current)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }
    this.startActivity(intent)
}