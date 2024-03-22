package top.chengdongqing.weui.feature.demos.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.isVideo
import top.chengdongqing.weui.core.ui.components.loading.WeLoadMore
import top.chengdongqing.weui.core.ui.components.mediapreview.previewMedias
import top.chengdongqing.weui.core.ui.components.screen.WeScreen
import top.chengdongqing.weui.core.utils.ChineseDateWeekFormatter
import top.chengdongqing.weui.core.utils.RequestMediaPermission
import top.chengdongqing.weui.core.utils.format
import top.chengdongqing.weui.core.utils.loadMediaThumbnail
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GalleryScreen(navController: NavController) {
    WeScreen(
        title = "Gallery",
        description = "相册",
        padding = PaddingValues(0.dp),
        scrollEnabled = false
    ) {
        RequestMediaPermission(onRevoked = {
            navController.popBackStack()
        }) {
            val state = rememberGalleryState()
            if (state.isLoading) {
                WeLoadMore()
            }

            val context = LocalContext.current
            LazyVerticalGrid(
                columns = GridCells.Adaptive(100.dp),
                contentPadding = PaddingValues(bottom = 60.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                state.mediaGroups.forEach { (date, items) ->
                    val title = date.format(DateTimeFormatter.ofPattern(ChineseDateWeekFormatter))
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = title,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        )
                    }
                    itemsIndexed(items) { index, item ->
                        MediaItem(item) {
                            context.previewMedias(items, index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MediaItem(media: MediaItem, onClick: () -> Unit) {
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
            contentDescription = "Gallery Item",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        if (media.isVideo()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
                    .background(Color(0f, 0f, 0f, 0.3f), RoundedCornerShape(16.dp))
                    .padding(vertical = 3.dp, horizontal = 6.dp)
            ) {
                Text(
                    text = media.duration.milliseconds.format(),
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}