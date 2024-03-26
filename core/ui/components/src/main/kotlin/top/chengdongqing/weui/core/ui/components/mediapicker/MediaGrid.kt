package top.chengdongqing.weui.core.ui.components.mediapicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.isVideo
import top.chengdongqing.weui.core.ui.components.mediapreview.previewMedias
import top.chengdongqing.weui.core.utils.clickableWithoutRipple
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.loadMediaThumbnail
import top.chengdongqing.weui.core.utils.showToast
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun ColumnScope.MediaGrid(state: MediaPickerState) {
    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.weight(1f)
    ) {
        itemsIndexed(state.mediaList) { index, item ->
            val selectedIndex = state.selectedMediaList.indexOf(item)
            val selected = selectedIndex != -1

            MediaGridCell(
                item,
                selected,
                selectedIndex,
                onClick = {
                    context.previewMedias(state.mediaList, index)
                }
            ) {
                if (selectedIndex == -1) {
                    if (state.selectedMediaList.size < state.count) {
                        state.add(item)
                    } else {
                        context.showToast("你最多只能选择${state.count}个")
                    }
                } else {
                    state.removeAt(selectedIndex)
                }
            }
        }
    }
}

@Composable
private fun MediaGridCell(
    media: MediaItem,
    selected: Boolean,
    selectedIndex: Int,
    onClick: () -> Unit,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.outline)
            .clickable { onClick() }
    ) {
        val context = LocalContext.current
        val thumbnail by produceState<Any?>(initialValue = null) {
            value = context.loadMediaThumbnail(media)
        }

        AsyncImage(
            model = thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        // 视频标识及时长
        if (media.isVideo()) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Videocam,
                    contentDescription = "视频",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = media.duration.milliseconds.format(),
                    color = Color.White,
                    fontSize = 15.sp
                )
            }
        }
        // 遮罩层
        if (selected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0f, 0f, 0f, 0.4f))
            )
        }
        // 选择框
        MediaCheckbox(selected, selectedIndex, onSelect)
    }
}

@Composable
private fun BoxScope.MediaCheckbox(selected: Boolean, selectedIndex: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .clickableWithoutRipple { onClick() }
            .padding(top = 6.dp, end = 6.dp, start = 18.dp, bottom = 18.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .selectable(selected),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Text(
                    text = (selectedIndex + 1).toString(),
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun Modifier.selectable(selected: Boolean) = this.then(
    if (selected) {
        Modifier.background(MaterialTheme.colorScheme.primary, CircleShape)
    } else {
        Modifier.border(1.dp, Color.White, CircleShape)
    }
)