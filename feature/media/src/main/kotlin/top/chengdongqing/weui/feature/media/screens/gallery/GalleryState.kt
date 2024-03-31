package top.chengdongqing.weui.feature.media.screens.gallery

import android.content.Context
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import top.chengdongqing.core.data.repository.LocalMediaRepositoryImpl
import top.chengdongqing.weui.core.data.model.MediaItem
import top.chengdongqing.weui.core.data.model.MediaType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Stable
interface GalleryState {
    /**
     * 根据日期分组的媒体数据
     */
    val mediaGroups: Map<LocalDate, List<MediaItem>>

    /**
     * 是否加载中
     */
    val isLoading: Boolean

    /**
     * 刷新数据
     */
    suspend fun refresh(types: Array<MediaType> = arrayOf(MediaType.IMAGE, MediaType.VIDEO))

    /**
     * 滚动到指定日期
     */
    suspend fun LazyGridState.scrollToDate(targetDate: LocalDate)
}

@Composable
fun rememberGalleryState(): GalleryState {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return remember {
        GalleryStateImpl(context).apply {
            coroutineScope.launch {
                refresh()
            }
        }
    }
}

private class GalleryStateImpl(context: Context) : GalleryState {
    override var mediaGroups by mutableStateOf<Map<LocalDate, List<MediaItem>>>(emptyMap())
    override var isLoading by mutableStateOf(true)

    override suspend fun refresh(types: Array<MediaType>) {
        isLoading = true
        mediaGroups = mediaRepository.loadMediaList(types)
            .groupBy {
                Instant.ofEpochSecond(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
            }
            .toSortedMap(compareByDescending { it })
            .mapValues { (_, value) -> value.sortedByDescending { it.date } }
        calculateTitleIndexMap()
        isLoading = false
    }

    private fun calculateTitleIndexMap() {
        var currentIndex = 0
        mediaGroups.forEach { (date, items) ->
            dateToIndexMap[date] = currentIndex
            currentIndex += 1 + items.size
        }
    }

    override suspend fun LazyGridState.scrollToDate(targetDate: LocalDate) {
        val targetIndex = dateToIndexMap[targetDate] ?: run {
            if (targetDate.isAfter(mediaGroups.keys.first())) 0
            else layoutInfo.totalItemsCount
        }
        scrollToItem(targetIndex)
    }

    private val mediaRepository = LocalMediaRepositoryImpl(context)
    private val dateToIndexMap = mutableMapOf<LocalDate, Int>()
}