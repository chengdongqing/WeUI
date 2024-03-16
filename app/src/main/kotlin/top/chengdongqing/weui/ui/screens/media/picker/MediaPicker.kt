package top.chengdongqing.weui.ui.screens.media.picker

import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyGridState
import org.burnoutcrew.reorderable.reorderable
import top.chengdongqing.weui.ui.components.screen.WeScreen
import top.chengdongqing.weui.utils.MediaType

@Composable
fun MediaPickerScreen(
    mediaListFlow: Flow<List<Uri>>,
    onChooseMedia: (type: MediaType, count: Int) -> Unit
) {
    WeScreen(
        title = "MediaPicker",
        description = "媒体选择器",
        containerColor = MaterialTheme.colorScheme.surface,
        scrollEnabled = false
    ) {
        val data = rememberSaveable(saver = UriListSaver) { mutableStateListOf() }
        val state = rememberReorderableLazyGridState(onMove = { from, to ->
            data.apply {
                add(to.index, removeAt(from.index))
            }
        }, canDragOver = { p1, _ ->
            p1.key != -1
        })

        LaunchedEffect(Unit) {
            mediaListFlow.collect {
                it.forEach { item ->
                    if (!data.contains(item)) {
                        data.add(item)
                    }
                }
            }
        }

        LazyVerticalGrid(
            state = state.gridState,
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.reorderable(state)
        ) {
            items(data, key = { it }) { item ->
                ReorderableItem(state, key = item) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 16.dp else 0.dp, label = "")
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .shadow(elevation)
                            .background(MaterialTheme.colorScheme.onSurface)
                            .detectReorderAfterLongPress(state)
                    ) {
                        AsyncImage(
                            model = item,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }
            }
            if (data.size < 9) {
                item(key = -1) {
                    PlusButton {
                        onChooseMedia(MediaType.IMAGE, 9 - data.size)
                    }
                }
            }
        }
    }
}

@Composable
private fun PlusButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.onSurface)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "添加",
            tint = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.size(60.dp)
        )
    }
}

private val UriListSaver: Saver<MutableList<Uri>, List<Uri>> = Saver(
    save = { stateList ->
        stateList.toList()
    },
    restore = { restoredList ->
        restoredList.toMutableList()
    }
)