package top.chengdongqing.weui.ui.views.media.gallery

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

data class Photo(
    val id: Int,
    val url: String,
    val highResUrl: String,
    val contentDescription: String
)

@Composable
fun Gallery1(photos: List<Photo>) {
    var activeId by rememberSaveable { mutableStateOf<Int?>(null) }
    val gridState = rememberLazyGridState()
    var autoScrollSpeed by remember { mutableStateOf(0f) }

    val scrim = remember(activeId) { FocusRequester() }

    Box {
        PhotoGrid(
            photos = photos,
            state = gridState,
            setAutoScrollSpeed = { autoScrollSpeed = it },
            navigateToPhoto = { activeId = it },
            modifier = Modifier.focusProperties { canFocus = activeId == null }
        )

        if (activeId != null) {
            FullScreenPhoto(
                photo = photos.first { it.id == activeId },
                onDismiss = { activeId = null },
                modifier = Modifier.focusRequester(scrim)
            )

            LaunchedEffect(activeId) {
                scrim.requestFocus()
            }
        }
    }

    LaunchedEffect(autoScrollSpeed) {
        if (autoScrollSpeed != 0f) {
            while (isActive) {
                gridState.scrollBy(autoScrollSpeed)
                delay(10)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PhotoGrid(
    photos: List<Photo>,
    state: LazyGridState,
    modifier: Modifier = Modifier,
    setAutoScrollSpeed: (Float) -> Unit = { },
    navigateToPhoto: (Int) -> Unit = { }
) {
    var selectedIds by rememberSaveable { mutableStateOf(emptySet<Int>()) }
    val inSelectionMode by remember { derivedStateOf { selectedIds.isNotEmpty() } }

    LazyVerticalGrid(
        state = state,
        columns = GridCells.Adaptive(128.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier.photoGridDragHandler(
            lazyGridState = state,
            selectedIds = { selectedIds },
            setSelectedIds = { selectedIds = it },
            setAutoScrollSpeed = setAutoScrollSpeed,
            autoScrollThreshold = with(LocalDensity.current) { 40.dp.toPx() }
        )
    ) {
        items(photos, key = { it.id }) { photo ->
            val selected by remember { derivedStateOf { selectedIds.contains(photo.id) } }
            PhotoItem(
                photo, inSelectionMode, selected,
                Modifier
                    .semantics {
                        if (!inSelectionMode) {
                            onLongClick("Select") {
                                selectedIds += photo.id
                                true
                            }
                        }
                    }
                    .then(if (inSelectionMode) {
                        Modifier.toggleable(
                            value = selected,
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null, // do not show a ripple
                            onValueChange = {
                                if (it) {
                                    selectedIds += photo.id
                                } else {
                                    selectedIds -= photo.id
                                }
                            }
                        )
                    } else {
//            Modifier.clickable { navigateToPhoto(photo.id) }
                        Modifier.combinedClickable(
                            onClick = { navigateToPhoto(photo.id) },
                            onLongClick = { selectedIds += photo.id }
                        )
                    })
            )
        }
    }
}

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.photoGridDragHandler(
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

@Composable
private fun PhotoItem(
    photo: Photo,
    inSelectionMode: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        tonalElevation = 3.dp
    ) {
        Box {
            val transition = updateTransition(selected, label = "selected")
            val padding by transition.animateDp(label = "padding") { selected ->
                if (selected) 10.dp else 0.dp
            }
            val roundedCornerShape by transition.animateDp(label = "corner") { selected ->
                if (selected) 16.dp else 0.dp
            }
            Image(
                painter = rememberAsyncImagePainter(photo.url),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .padding(padding)
                    .clip(RoundedCornerShape(roundedCornerShape))
            )
            if (inSelectionMode) {
                if (selected) {
                    val bgColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    Icon(
                        Icons.Filled.CheckCircle,
                        tint = MaterialTheme.colorScheme.primary,
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
}

@Composable
private fun FullScreenPhoto(
    photo: Photo,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Scrim(onDismiss, Modifier.fillMaxSize())
        PhotoImage(photo)
    }
}

@Composable
private fun Scrim(onClose: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxSize()
            .pointerInput(onClose) { detectTapGestures { onClose() } }
            .semantics {
                onClick("关闭") { onClose(); true }
            }
            .focusable()
            .onKeyEvent {
                if (it.key == Key.Escape) {
                    onClose(); true
                } else {
                    false
                }
            }
            .background(Color.DarkGray.copy(alpha = 0.75f))
    )
}

@Composable
fun PhotoImage(photo: Photo, modifier: Modifier = Modifier) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableStateOf(1f) }

    Image(
        painter = rememberAsyncImagePainter(model = photo.highResUrl),
        contentDescription = photo.contentDescription,
        modifier
            .clipToBounds()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        zoom = if (zoom > 1f) 1f else 2f
                        offset = calculateDoubleTapOffset(zoom, size, tapOffset)
                    }
                )
            }
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { centroid, pan, gestureZoom, _ ->
                        offset = offset.calculateNewOffset(
                            centroid, pan, zoom, gestureZoom, size
                        )
                        zoom = maxOf(1f, zoom * gestureZoom)
                    }
                )
            }
            .graphicsLayer {
                translationX = -offset.x * zoom
                translationY = -offset.y * zoom
                scaleX = zoom; scaleY = zoom
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .aspectRatio(1f)
    )
}

private fun Offset.calculateNewOffset(
    centroid: Offset,
    pan: Offset,
    zoom: Float,
    gestureZoom: Float,
    size: IntSize
): Offset {
    val newScale = maxOf(1f, zoom * gestureZoom)
    val newOffset = (this + centroid / zoom) -
            (centroid / newScale + pan / zoom)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}

private fun calculateDoubleTapOffset(
    zoom: Float,
    size: IntSize,
    tapOffset: Offset
): Offset {
    val newOffset = Offset(tapOffset.x, tapOffset.y)
    return Offset(
        newOffset.x.coerceIn(0f, (size.width / zoom) * (zoom - 1f)),
        newOffset.y.coerceIn(0f, (size.height / zoom) * (zoom - 1f))
    )
}