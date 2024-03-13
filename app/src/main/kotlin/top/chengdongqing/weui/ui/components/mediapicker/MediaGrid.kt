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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import top.chengdongqing.weui.ui.screens.demo.gallery.MediaItem
import top.chengdongqing.weui.ui.screens.demo.gallery.produceThumbnail
import top.chengdongqing.weui.ui.theme.PrimaryColor
import top.chengdongqing.weui.utils.formatDuration

@Composable
internal fun ColumnScope.MediaGrid(pickerViewModel: MediaPickerViewModel) {
    val mediaList by pickerViewModel.allMedias.collectAsState(initial = emptyList())

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.weight(1f)
    ) {
        items(mediaList) {
            MediaGridCell(it) {

            }
        }
    }
}

@Composable
private fun MediaGridCell(item: MediaItem, onClick: () -> Unit) {
    var checked by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(MaterialTheme.colorScheme.outline)
            .clickable {
                onClick()
                checked = !checked
            }
    ) {
        AsyncImage(
            model = produceThumbnail(item).value,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
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
        MediaCheckbox(checked)
    }
}

@Composable
private fun BoxScope.MediaCheckbox(checked: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .align(Alignment.TopEnd)
            .offset(x = (-6).dp, y = 6.dp)
            .checkable(checked),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Text(text = "1", color = Color.White, fontSize = 13.sp)
        }
    }
}

private fun Modifier.checkable(checked: Boolean) = this.then(
    if (checked) {
        Modifier.background(PrimaryColor, CircleShape)
    } else {
        Modifier.border(1.dp, Color.White, CircleShape)
    }
)