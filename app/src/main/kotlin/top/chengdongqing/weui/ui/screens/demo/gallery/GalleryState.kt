package top.chengdongqing.weui.ui.screens.demo.gallery

import android.content.Context
import android.os.Build
import android.util.Size
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
import kotlinx.coroutines.withContext
import top.chengdongqing.weui.data.model.MediaItem
import top.chengdongqing.weui.data.model.isImage
import top.chengdongqing.weui.data.model.isVideo
import top.chengdongqing.weui.data.repository.LocalMediaRepositoryImpl
import top.chengdongqing.weui.utils.loadVideoThumbnail
import java.io.IOException
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

    /**
     * 获取缩略图
     */
    suspend fun getThumbnail(media: MediaItem): Any?
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
    private val context: Context,
    coroutineScope: CoroutineScope
) : GalleryState {
    override val mediaGroups: Map<LocalDate, List<MediaItem>>
        get() = _mediaGroups
    override val isLoading: Boolean
        get() = _isLoading

    override suspend fun getThumbnail(media: MediaItem): Any? {
        // 图片在低版本系统中加载原图
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && !media.isVideo()) {
            return media.uri
        } else {
            return withContext(Dispatchers.IO) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // 高版本系统直接加载缩略图
                        context.contentResolver.loadThumbnail(
                            media.uri, Size(200, 200), null
                        )
                    } else {
                        // 低版本系统获取视频首帧
                        context.loadVideoThumbnail(media.uri)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    if (media.isImage()) {
                        media.uri
                    } else {
                        null
                    }
                }
            }
        }
    }

    init {
        coroutineScope.launch(Dispatchers.IO) {
            val mediaRepository = LocalMediaRepositoryImpl(context)
            _mediaGroups = mediaRepository.loadAllMedias().groupBy {
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