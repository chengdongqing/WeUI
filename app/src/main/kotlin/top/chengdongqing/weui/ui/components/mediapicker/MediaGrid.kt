package top.chengdongqing.weui.ui.components.mediapicker

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.chengdongqing.weui.ui.screens.demo.gallery.MediaItem
import top.chengdongqing.weui.ui.screens.demo.gallery.produceThumbnail
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.MediaType
import top.chengdongqing.weui.utils.clickableWithoutRipple
import top.chengdongqing.weui.utils.formatDuration
import top.chengdongqing.weui.utils.previewMedias

@Composable
internal fun ColumnScope.MediaGrid(pickerViewModel: MediaPickerViewModel) {
    val context = LocalContext.current
    val mediaList by pickerViewModel.mediaList.collectAsState(initial = emptyList())
    val filteredMediaList = remember(mediaList, pickerViewModel.mediaType) {
        if (pickerViewModel.mediaType == null) {
            mediaList
        } else {
            mediaList.filter {
                if (pickerViewModel.mediaType == MediaType.IMAGE) {
                    !it.isVideo
                } else {
                    it.isVideo
                }
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.weight(1f)
    ) {
        itemsIndexed(filteredMediaList) { index, item ->
            val selectIndex = pickerViewModel.selectedList.indexOf(item.path)
            val selected = selectIndex != -1

            MediaGridCell(
                item,
                selected,
                selectIndex,
                onPreview = {
                    context.previewMedias(filteredMediaList.map { it.path }, index)
                }
            ) {
                if (selectIndex == -1) {
                    if (pickerViewModel.selectedList.size < pickerViewModel.countLimits) {
                        pickerViewModel.selectedList.add(item.path)
                    }
                } else {
                    pickerViewModel.selectedList.removeAt(selectIndex)
                }
            }
        }
    }
}

@Composable
private fun MediaGridCell(
    item: MediaItem,
    selected: Boolean,
    selectIndex: Int,
    onPreview: () -> Unit,
    onSelect: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.outline)
            .clickable { onPreview() }
    ) {
        AsyncImage(
            model = produceThumbnail(item).value,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        // 视频标识及时长
        if (item.isVideo) {
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
                    text = formatDuration(item.duration),
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
                    .background(Color(0f, 0f, 0f, 0.3f))
            )
        }
        // 选择框
        MediaCheckbox(selected, selectIndex, onSelect)
    }
}

@Composable
private fun BoxScope.MediaCheckbox(selected: Boolean, selectIndex: Int, onClick: () -> Unit) {
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
                    text = (selectIndex + 1).toString(),
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }
    }
}

private fun Modifier.selectable(selected: Boolean) = this.then(
    if (selected) {
        Modifier.background(PrimaryColor, CircleShape)
    } else {
        Modifier.border(1.dp, Color.White, CircleShape)
    }
)