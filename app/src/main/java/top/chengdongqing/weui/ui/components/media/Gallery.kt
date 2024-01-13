package top.chengdongqing.weui.ui.components.media

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.form.ButtonSize
import top.chengdongqing.weui.ui.components.form.ButtonType
import top.chengdongqing.weui.ui.components.form.WeButton

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeGallery() {
    val context = LocalContext.current
    val selectedMedias = remember {
        mutableStateListOf<MediaItem>()
    }

    fun setMedias(uris: List<Uri>) {
        if (uris.isNotEmpty()) {
            selectedMedias.clear()
            selectedMedias.addAll(uris.map {
                MediaItem(it, context)
            })
        }
    }

    val pickMultipleMediaLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) {
            setMedias(it)
        }
    val readStoragePermissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeButton("选择本地媒体文件", type = ButtonType.PLAIN, size = ButtonSize.SMALL) {
                pickMultipleMediaLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                )
            }

            val coroutineScope = rememberCoroutineScope()
            WeButton("读取本地媒体文件", type = ButtonType.PLAIN, size = ButtonSize.SMALL) {
                if (readStoragePermissionState.status.isGranted) {
                    coroutineScope.launch {
                        setMedias(queryImages(context))
                    }
                } else {
                    readStoragePermissionState.launchPermissionRequest()
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        MediaGrid(selectedMedias)
    }
}

private suspend fun queryImages(context: Context): List<Uri> = withContext(Dispatchers.IO) {
    val imageList = mutableListOf<Uri>()
    val projection = arrayOf(
        MediaStore.Images.ImageColumns._ID
    )
    val cursor = context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"
    )

    cursor?.use {
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imageList.add(uri)
        }
    }

    imageList
}

@Composable
fun MediaGrid(mediaItems: List<MediaItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(mediaItems) { item ->
            AsyncImage(
                model = if (item.type == ContentType.Video) {
                    rememberVideoThumbnail(item.uri).value
                } else {
                    item.uri
                },
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
            )
        }
    }
}

@Composable
fun rememberVideoThumbnail(videoUri: Uri): MutableState<Bitmap?> {
    val context = LocalContext.current
    val thumbnailState = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(videoUri) {
        val thumbnail = MediaMetadataRetriever().run {
            setDataSource(context, videoUri)
            extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
            extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
            getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        }
        thumbnailState.value = thumbnail
    }

    return thumbnailState
}

enum class ContentType {
    Image,
    Video
}

data class MediaItem(val uri: Uri, val type: ContentType) {
    constructor(uri: Uri, context: Context) : this(uri, getContentType(uri, context))

    private companion object {
        fun getContentType(uri: Uri, context: Context): ContentType {
            val mimeType = context.contentResolver.getType(uri)
            return when {
                mimeType?.startsWith("video") == true -> ContentType.Video
                else -> ContentType.Image
            }
        }
    }
}