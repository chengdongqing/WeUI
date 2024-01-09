package top.chengdongqing.weui.ui.components.media

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.ui.components.form.ButtonSize
import top.chengdongqing.weui.ui.components.form.WeButton

@Composable
fun WeGallery() {
    val context = LocalContext.current
    val selectedMedia = remember {
        mutableStateListOf<MediaItem>()
    }
    val pickMultipleMediaLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isNotEmpty()) {
                selectedMedia.clear()
                selectedMedia.addAll(uris.map {
                    MediaItem(it, context)
                })
            }
        }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WeButton("选择本地媒体文件", size = ButtonSize.SMALL) {
                pickMultipleMediaLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                )
            }
            WeButton("读取本地媒体文件", size = ButtonSize.SMALL) {
                Toast.makeText(context, "敬请期待", Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(Modifier.height(20.dp))
        MediaGrid(selectedMedia)
    }
}

@Composable
fun MediaGrid(mediaItems: List<MediaItem>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(mediaItems.size) { index ->
            val mediaItem = mediaItems[index]
            when (mediaItem.type) {
                ContentType.Image -> {
                    val imageBitmap = rememberImageBitmap(mediaItem.uri)
                    imageBitmap.value?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f)
                        )
                    }
                }

                ContentType.Video -> {
                    val videoThumbnail = rememberVideoThumbnail(mediaItem.uri)
                    videoThumbnail.value?.let { thumbnail ->
                        Image(
                            bitmap = thumbnail.asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun rememberVideoThumbnail(videoUri: Uri): MutableState<Bitmap?> {
    val ctx = LocalContext.current
    val thumbnailState = remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(videoUri) {
        val thumbnail = createVideoThumbnail(ctx, videoUri)
        thumbnailState.value = thumbnail
    }

    return thumbnailState
}

private fun createVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return MediaMetadataRetriever().run {
        setDataSource(context, uri)
        extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
        extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
        getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
    }
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

@Composable
fun rememberImageBitmap(imageUri: Uri): MutableState<ImageBitmap?> {
    val ctx = LocalContext.current
    val bitmapState = remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageUri) {
        val bitmap = loadBitmapFromUri(ctx, imageUri)
        bitmapState.value = bitmap
    }

    return bitmapState
}

suspend fun loadBitmapFromUri(ctx: Context, uri: Uri): ImageBitmap? {
    return withContext(Dispatchers.IO) {
        val inputStream = ctx.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        bitmap?.asImageBitmap()
    }
}
