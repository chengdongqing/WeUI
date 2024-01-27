package top.chengdongqing.weui.ui.views.media.gallery

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toIntRect
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okio.IOException
import top.chengdongqing.weui.ui.components.Page
import top.chengdongqing.weui.ui.components.basic.WeLoadMore
import top.chengdongqing.weui.ui.theme.LightColor
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.formatDuration
import java.util.Date
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GalleryPage(galleryViewModel: GalleryViewModel, navController: NavController) {
    Page(title = "Gallery", description = "相册", padding = PaddingValues(0.dp)) {
        val context = LocalContext.current
        var loading by remember { mutableStateOf(true) }
        val multiplePermissionsState = rememberMultiplePermissionsState(
            permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            } else {
                listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        )

        LaunchedEffect(Unit) {
            snapshotFlow {
                multiplePermissionsState.allPermissionsGranted
            }.collect { allPermissionsGranted ->
                if (allPermissionsGranted) {
                    delay(300)
                    galleryViewModel.setItems(queryMedias(context))
                    loading = false
                } else {
                    multiplePermissionsState.launchMultiplePermissionRequest()
                }
            }
        }

        if (loading) {
            WeLoadMore()
        } else {
            MediasGrid(galleryViewModel.mediaItems) {
                navController.navigate("media-preview?index=$it")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MediasGrid(
    mediaItems: List<MediaItem>,
    navigateToPreview: (Int) -> Unit
) {
    val gridState = rememberLazyGridState()
    var autoScrollSpeed by remember { mutableFloatStateOf(0f) }
    var selectedIds by remember { mutableStateOf(emptySet<Int>()) }
    val inSelectionMode by remember { derivedStateOf { selectedIds.isNotEmpty() } }

    LaunchedEffect(autoScrollSpeed) {
        if (autoScrollSpeed != 0f) {
            while (isActive) {
                gridState.scrollBy(autoScrollSpeed)
                delay(10)
            }
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier
            .photoGridDragHandler(
                lazyGridState = gridState,
                selectedIds = { selectedIds },
                setSelectedIds = { selectedIds = it },
                setAutoScrollSpeed = { autoScrollSpeed = it },
                autoScrollThreshold = with(LocalDensity.current) { 40.dp.toPx() }
            )
    ) {
        itemsIndexed(mediaItems,
            key = { index, _ -> index }
        ) { index, item ->
            val selected by remember { derivedStateOf { selectedIds.contains(index) } }
            MediaThumbnail(
                item,
                inSelectionMode,
                selected,
                Modifier
                    .aspectRatio(1f)
                    .background(Color.LightGray)
                    .then(if (inSelectionMode) {
                        Modifier.toggleable(
                            value = selected,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onValueChange = {
                                if (it) {
                                    selectedIds += index
                                } else {
                                    selectedIds -= index
                                }
                            }
                        )
                    } else {
                        Modifier.combinedClickable(
                            onClick = {
                                navigateToPreview(index)
                            },
                            onLongClick = { selectedIds += index }
                        )
                    })
            )
        }
    }
}

@Composable
fun MediaThumbnail(
    item: MediaItem,
    inSelectionMode: Boolean,
    selected: Boolean,
    modifier: Modifier
) {
    val context = LocalContext.current

    Box(modifier) {
        produceState<Any?>(initialValue = null, item.uri) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !item.isVideo) {
                // 低版本系统直接加载原图
                value = item.uri
            } else {
                value = withContext(Dispatchers.IO) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            // 高版本系统直接加载缩略图
                            context.contentResolver.loadThumbnail(
                                item.uri,
                                Size(300, 300),
                                null
                            )
                        } else {
                            // 低版本系统获取视频首帧截图
                            MediaMetadataRetriever().run {
                                setDataSource(context, item.uri)
                                extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
                                extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
                                getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("Gallery", "Thumbnail load exception", e)
                        null
                    }
                }
            }
        }.value?.let {
            val transition = updateTransition(selected, label = "selected")
            val padding by transition.animateDp(label = "padding") { selected ->
                if (selected) 12.dp else 0.dp
            }
            val roundedCornerShape by transition.animateDp(label = "corner") { selected ->
                if (selected) 12.dp else 0.dp
            }

            AsyncImage(
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .padding(padding)
                    .clip(RoundedCornerShape(roundedCornerShape))
            )
        }
        if (item.isVideo) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .background(LightColor, RoundedCornerShape(16.dp))
                    .padding(vertical = 3.dp, horizontal = 6.dp)
            ) {
                Text(
                    text = formatDuration(item.duration),
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }

        if (inSelectionMode) {
            if (selected) {
                val bgColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                Icon(
                    Icons.Filled.CheckCircle,
                    tint = PrimaryColor,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(4.dp)
                        .border(2.dp, bgColor, CircleShape)
                        .clip(CircleShape)
                        .background(bgColor)
                )
            } else {
                Icon(
                    Icons.Filled.CheckCircle,
                    tint = Color.White.copy(alpha = 0.7f),
                    contentDescription = null,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.photoGridDragHandler(
    lazyGridState: LazyGridState,
    selectedIds: () -> Set<Int>,
    autoScrollThreshold: Float,
    setSelectedIds: (Set<Int>) -> Unit = { },
    setAutoScrollSpeed: (Float) -> Unit = { },
) = pointerInput(autoScrollThreshold, setSelectedIds, setAutoScrollSpeed) {
    fun photoIdAtOffset(hitPoint: Offset): Int? =
        lazyGridState.layoutInfo.visibleItemsInfo.find { itemInfo ->
            itemInfo.size.toIntRect().contains(hitPoint.round() - itemInfo.offset)
        }?.key as? Int

    var initialPhotoId: Int? = null
    var currentPhotoId: Int? = null
    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            photoIdAtOffset(offset)?.let { key ->
                if (!selectedIds().contains(key)) {
                    initialPhotoId = key
                    currentPhotoId = key
                    setSelectedIds(selectedIds() + key)
                }
            }
        },
        onDragCancel = { setAutoScrollSpeed(0f); initialPhotoId = null },
        onDragEnd = { setAutoScrollSpeed(0f); initialPhotoId = null },
        onDrag = { change, _ ->
            if (initialPhotoId != null) {
                val distFromBottom =
                    lazyGridState.layoutInfo.viewportSize.height - change.position.y
                val distFromTop = change.position.y
                setAutoScrollSpeed(
                    when {
                        distFromBottom < autoScrollThreshold -> autoScrollThreshold - distFromBottom
                        distFromTop < autoScrollThreshold -> -(autoScrollThreshold - distFromTop)
                        else -> 0f
                    }
                )

                photoIdAtOffset(change.position)?.let { pointerPhotoId ->
                    if (currentPhotoId != pointerPhotoId) {
                        setSelectedIds(
                            selectedIds().addOrRemoveUpTo(
                                pointerPhotoId,
                                currentPhotoId,
                                initialPhotoId
                            )
                        )
                        currentPhotoId = pointerPhotoId
                    }
                }
            }
        }
    )
}

private fun Set<Int>.addOrRemoveUpTo(
    pointerKey: Int?,
    previousPointerKey: Int?,
    initialKey: Int?
): Set<Int> {
    return if (pointerKey == null || previousPointerKey == null || initialKey == null) {
        this
    } else {
        this
            .minus(initialKey..previousPointerKey)
            .minus(previousPointerKey..initialKey)
            .plus(initialKey..pointerKey)
            .plus(pointerKey..initialKey)
    }
}

private suspend fun queryMedias(context: Context): List<MediaItem> =
    withContext(Dispatchers.IO) {
        val mediaItems = mutableListOf<MediaItem>()
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DURATION,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.MIME_TYPE
        )
        context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            MediaStore.Files.FileColumns.MEDIA_TYPE + "=? OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?",
            arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            ),
            MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID)
            val nameColumn =
                cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndex(MediaStore.Files.FileColumns.DURATION)
            val sizeColumn =
                cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)
            val dateColumn =
                cursor.getColumnIndex(MediaStore.Files.FileColumns.DATE_ADDED)
            val mediaTypeColumn =
                cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
            val mimeTypeColumn =
                cursor.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
            val dataColumn =
                cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA)

            while (cursor.moveToNext()) {
                val uri = ContentUris.withAppendedId(
                    MediaStore.Files.getContentUri("external"),
                    cursor.getLong(idColumn)
                )
                mediaItems.add(
                    MediaItem(
                        uri,
                        name = cursor.getString(nameColumn),
                        isVideo = cursor.getInt(mediaTypeColumn) == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO,
                        mimeType = cursor.getString(mimeTypeColumn),
                        duration = cursor.getLong(durationColumn)
                            .toDuration(DurationUnit.MILLISECONDS),
                        size = cursor.getLong(sizeColumn),
                        date = Date(cursor.getLong(dateColumn)),
                        path = cursor.getString(dataColumn)
                    )
                )
            }
        }

        mediaItems
    }

data class MediaItem(
    val uri: Uri,
    val name: String,
    val isVideo: Boolean,
    val mimeType: String,
    val duration: Duration,
    val size: Long,
    val date: Date,
    val path: String
)

class GalleryViewModel : ViewModel() {
    var mediaItems by mutableStateOf<List<MediaItem>>(emptyList())
        private set

    fun setItems(items: List<MediaItem>) {
        mediaItems = items
    }
}