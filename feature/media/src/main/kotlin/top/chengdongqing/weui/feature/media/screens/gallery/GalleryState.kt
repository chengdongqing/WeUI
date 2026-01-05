package top.chengdongqing.weui.feature.media.screens.gallery

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
     * 手动触发刷新
     */
    fun requestRefresh(showLoading: Boolean = true)

    /**
     * 获取日期对应的列表索引（用于滚动）
     */
    fun getIndexForDate(date: LocalDate): Int

    /**
     * 更新过滤类型
     */
    fun updateTypes(types: Array<MediaType>)
}

@Composable
fun rememberGalleryState(
    types: Array<MediaType> = arrayOf(MediaType.IMAGE, MediaType.VIDEO)
): GalleryState {
    val context = LocalContext.current.applicationContext
    val scope = rememberCoroutineScope()

    val state = remember { GalleryStateImpl(context, scope, types) }

    LaunchedEffect(types) {
        state.updateTypes(types)
    }

    return state
}

private class GalleryStateImpl(
    private val context: Context,
    private val scope: CoroutineScope,
    initialTypes: Array<MediaType>
) : GalleryState {
    override var mediaGroups by mutableStateOf<Map<LocalDate, List<MediaItem>>>(emptyMap())
        private set
    override var isLoading by mutableStateOf(false)
        private set

    private var _currentTypes by mutableStateOf(initialTypes)
    private val mediaRepository = LocalMediaRepositoryImpl(context)
    private val dateToIndexMap = mutableMapOf<LocalDate, Int>()
    private var refreshJob: Job? = null

    init {
        // 初始化数据
        requestRefresh()
        // 监听相册变动
        observeMediaStore()
    }

    override fun updateTypes(types: Array<MediaType>) {
        _currentTypes = types
    }

    override fun requestRefresh(showLoading: Boolean) {
        if (showLoading) {
            isLoading = true
        }

        // 300ms 内连续触发只会执行最后一次
        refreshJob?.cancel()
        refreshJob = scope.launch {
            delay(300)
            loadMedias(_currentTypes)

            if (showLoading) {
                isLoading = false
            }
        }
    }

    private suspend fun loadMedias(types: Array<MediaType>) {
        // 在后台线程进行CPU密集的排序和分组计算
        val result = withContext(Dispatchers.Default) {
            val rawList = mediaRepository.loadMediaList(types)

            // 按日期倒序排列，内部图片按时间倒序
            val grouped = rawList.groupBy {
                Instant.ofEpochSecond(it.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            }.toSortedMap(compareByDescending { it })

            // 预计算索引：Header 占 1 位，Items 占 N 位
            val newIndexMap = mutableMapOf<LocalDate, Int>()
            var currentIndex = 0
            grouped.forEach { (date, items) ->
                newIndexMap[date] = currentIndex
                currentIndex += 1 + items.size
            }
            grouped to newIndexMap
        }

        // 回到主线程更新 UI 状态
        mediaGroups = result.first
        dateToIndexMap.clear()
        dateToIndexMap.putAll(result.second)
    }

    private fun observeMediaStore() {
        // 创建后台线程
        val handlerThread = HandlerThread("MediaStoreObserver").apply { start() }
        val backgroundHandler = Handler(handlerThread.looper)
        val observer = object : ContentObserver(backgroundHandler) {
            override fun onChange(selfChange: Boolean, uri: Uri?) {
                requestRefresh(false)
            }
        }

        val resolver = context.contentResolver
        // 同时监听图片和视频
        resolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            observer
        )
        resolver.registerContentObserver(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            true,
            observer
        )

        // 利用协程作用域的 Job 监听销毁，自动解绑 Observer 避免内存泄漏
        scope.coroutineContext[Job]?.invokeOnCompletion {
            resolver.unregisterContentObserver(observer)
            handlerThread.quitSafely() // 退出后台线程
        }
    }

    override fun getIndexForDate(date: LocalDate): Int = dateToIndexMap[date] ?: 0
}
