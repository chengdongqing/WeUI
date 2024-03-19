package top.chengdongqing.weui.core.ui.components.mediapicker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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

@Stable
interface MediaPickerState {
    /**
     * 可选择的媒体类型
     */
    val type: MediaType?

    /**
     * 可选择的最大数量
     */
    val count: Int

    /**
     * 类型是否可以切换
     */
    val isTypeEnabled: Boolean

    /**
     * 是否加载中
     */
    val isLoading: Boolean

    /**
     * 可选择的媒体文件列表
     */
    val mediaList: List<MediaItem>

    /**
     * 已选择的媒体文件列表
     */
    val selectedMediaList: List<MediaItem>

    /**
     * 添加选择项
     */
    fun add(media: MediaItem)

    /**
     * 删除选择项
     */
    fun removeAt(index: Int)

    /**
     * 刷新可选项
     */
    fun refresh(type: MediaType?)
}

@Composable
fun rememberMediaPickerState(type: MediaType?, count: Int): MediaPickerState {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    return remember {
        MediaPickerStateImpl(context, coroutineScope, type, count).apply {
            refresh(type)
        }
    }
}

private class MediaPickerStateImpl(
    context: Context,
    private val coroutineScope: CoroutineScope,
    private val initialType: MediaType?,
    override val count: Int
) : MediaPickerState {
    override var type by mutableStateOf(initialType)
    override val isTypeEnabled: Boolean
        get() = initialType == null
    override var isLoading by mutableStateOf(true)
    override var mediaList by mutableStateOf<List<MediaItem>>(emptyList())
    override val selectedMediaList = mutableStateListOf<MediaItem>()

    override fun add(media: MediaItem) {
        selectedMediaList.add(media)
    }

    override fun removeAt(index: Int) {
        selectedMediaList.removeAt(index)
    }

    override fun refresh(type: MediaType?) {
        val types = buildList {
            if (type == null || type == MediaType.IMAGE) {
                add(MediaType.IMAGE)
            }
            if (type == null || type == MediaType.VIDEO) {
                add(MediaType.VIDEO)
            }
        }.toTypedArray()

        this.type = type
        this.isLoading = true
        coroutineScope.launch(Dispatchers.IO) {
            mediaList = mediaRepository.loadMediaList(types)
            isLoading = false
        }
    }

    private val mediaRepository = LocalMediaRepositoryImpl(context)
}