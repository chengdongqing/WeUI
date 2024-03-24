package top.chengdongqing.weui.feature.media.screens.gallery

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
     * 是否在加载中
     */
    val isLoading: Boolean
}

@Composable
fun rememberGalleryState(): GalleryState {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return remember {
        GalleryStateImpl(context, coroutineScope)
    }
}

private class GalleryStateImpl(
    context: Context,
    coroutineScope: CoroutineScope
) : GalleryState {
    override val mediaGroups: Map<LocalDate, List<MediaItem>>
        get() = _mediaGroups
    override val isLoading: Boolean
        get() = _isLoading

    init {
        val mediaRepository = LocalMediaRepositoryImpl(context)
        coroutineScope.launch(Dispatchers.IO) {
            _mediaGroups =
                mediaRepository.loadMediaList(types = arrayOf(MediaType.IMAGE, MediaType.VIDEO))
                    .groupBy {
                        Instant.ofEpochSecond(it.date).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    .toSortedMap(compareByDescending { it })
                    .mapValues { (_, value) -> value.sortedByDescending { it.date } }
            _isLoading = false
        }
    }

    private var _mediaGroups by mutableStateOf<Map<LocalDate, List<MediaItem>>>(emptyMap())
    private var _isLoading by mutableStateOf(true)
}